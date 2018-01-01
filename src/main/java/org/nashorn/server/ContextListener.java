package org.nashorn.server;

import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import javax.servlet.annotation.WebListener;

import java.util.concurrent.ExecutorService;


@WebListener
public class ContextListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(ContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        LOGGER.info("Context initialized.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        /*
            Graceful shutdown will be later
         */
        LOGGER.info("Context destroyed.");


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
