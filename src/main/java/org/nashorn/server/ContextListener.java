package org.nashorn.server;

import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


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
}
