package org.nashorn.server;


import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import javax.naming.Context;
import javax.naming.InitialContext;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            int corePoolSize = (Integer) envCtx.lookup("corePoolSize");
            System.out.println("CorePoolSize -> " + corePoolSize);
        } catch (NamingException ex) {
            ex.printStackTrace();
        }


    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        /*
            Graceful shutdown will be later
         */
        System.out.println("SHUTDOWN");
        CommonPool.getInstance().shutdown();
    }
}
