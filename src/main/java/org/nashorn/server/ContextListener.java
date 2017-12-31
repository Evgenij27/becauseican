package org.nashorn.server;

import javax.servlet.ServletContextEvent;
import org.apache.log4j.Logger;

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
import java.util.concurrent.TimeoutException;

@WebListener
public class ContextListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(ContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        LOGGER.info("Context initialized.");
	System.out.println(System.getProperty("java.class.path"));

        ServletContext context = servletContextEvent.getServletContext();

        /*
            Set up a factory and ship it to the servlet
         */


        /*LOGGER.info("Getting CommandResolverExecutor from JNDI");

        ExecutorService commandResolverExecutorPool = getExecutorFromJNDI("pool/CommandResolverExecutor");
        LOGGER.info("CommandResolverExecutor");
        LOGGER.info(commandResolverExecutorPool);

        ExecutorService commandExecutorPool = getExecutorFromJNDI("pool/CommandExecutor");
        LOGGER.info("CommandExecutor");
        LOGGER.info(commandExecutorPool);

        *//*
            Starting command resolver thread
        *//*
        Runnable commandResolver = null;
        Runnable commandExecutor = null;
        try {
            commandResolver = new CommandResolver(factory, "router", "command");
            commandExecutor = new CommandExecutor(factory, "command", null, commandExecutorPool);
        } catch (IOException ioex) {
            LOGGER.error("Cannot create connect to RabbitMQ");
            throw new RuntimeException(ioex);
        } catch (TimeoutException tex) {
            LOGGER.error("Timeout during connection to RabbitMQ");
            throw new RuntimeException(tex);
        }

        commandResolverExecutorPool.submit(commandResolver);
        commandResolverExecutorPool.submit(commandExecutor);

        LOGGER.info("CommandResolverRunnable");
        LOGGER.info(commandResolver);

        LOGGER.info("CommandExecutorRunnable");
        LOGGER.info(commandExecutor);

*/
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        /*
            Graceful shutdown will be later
         */
        LOGGER.info("Context destroyed.");

        /*ServletContext context = servletContextEvent.getServletContext();

        LOGGER.info("Shutdowning CommandResolverExecutor");
        ExecutorService commandResolverExecutor = getExecutorFromJNDI("pool/CommandResolverExecutor");

        LOGGER.info("CommandResolverExecutor");
        LOGGER.info(commandResolverExecutor);

        List<Runnable> runnableList = commandResolverExecutor.shutdownNow();
        LOGGER.debug(runnableList);*/

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
