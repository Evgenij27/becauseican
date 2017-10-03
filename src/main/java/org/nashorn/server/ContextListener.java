package org.nashorn.server;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.log4j.Logger;
import org.nashorn.server.intercom.RequestRecv;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

@WebListener
public class ContextListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(ContextListener.class);

    private static final String QUEUE_NAME = "hello";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        LOGGER.info("Context initialized.");

        ServletContext context = servletContextEvent.getServletContext();

        /*
            Set up a factory and ship it to the servlet
         */
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        context.setAttribute("RABBITMQ_FACTORY", factory);


        LOGGER.info("Getting CommandResolverExecutor from JNDI");

        ExecutorService commandResolverExecutor = getExecutorFromJNDI("pool/CommandResolverExecutor");
        LOGGER.info("CommandResolverExecutor");
        LOGGER.info(commandResolverExecutor);

        /*
            Starting consumer thread
        */
        Runnable commandResolver = null;
        try {
            commandResolver = new RequestRecv(factory, QUEUE_NAME);
        } catch (IOException ioex) {
            LOGGER.error("Cannot create connect to RabbitMQ");
            throw new RuntimeException(ioex);
        } catch (TimeoutException tex) {
            LOGGER.error("Timeout during connection to RabbitMQ");
            throw new RuntimeException(tex);
        }

        commandResolverExecutor.submit(commandResolver);
        LOGGER.info("CommandResolverRunnable");
        LOGGER.info(commandResolver);

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        /*
            Graceful shutdown will be later
         */
        LOGGER.info("Context destroyed.");

        ServletContext context = servletContextEvent.getServletContext();

        LOGGER.info("Shutdowning CommandResolverExecutor");
        ExecutorService commandResolverExecutor = getExecutorFromJNDI("pool/CommandResolverExecutor");

        LOGGER.info("CommandResolverExecutor");
        LOGGER.info(commandResolverExecutor);

        List<Runnable> runnableList = commandResolverExecutor.shutdownNow();
        LOGGER.debug(runnableList);

    }

    private ConnectionFactory initConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        return factory;
    }

    private ExecutorService getExecutorFromJNDI(String contextName) {
        ExecutorService executor = null;
        try {
            executor = (ExecutorService) getObjectFromJNDI(contextName);
        } catch (NamingException ex) {
            LOGGER.error(String.format("Cannot find ExecutorService with such context name %s", contextName), ex);
            throw new IllegalArgumentException(ex);
        }
        return executor;
    }

    private Object getObjectFromJNDI(String contextName) throws NamingException {
        Context initCtx = new InitialContext();
        Context envCtx = (Context) initCtx.lookup("java:comp/env");
        return envCtx.lookup(contextName);
    }
}
