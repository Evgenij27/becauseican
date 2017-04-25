package org.nashorn.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {

    private static CommandResolver resolver;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("**** LISTENER ****");
        resolver = CommandResolver.getInstance();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
