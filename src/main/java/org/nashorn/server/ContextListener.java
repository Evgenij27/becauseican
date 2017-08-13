package org.nashorn.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.*;
import java.util.Enumeration;
import java.util.Set;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ServletContext context = servletContextEvent.getServletContext();

        String baseDirectory = context.getInitParameter("baseDir");

        StringBuilder builder = new StringBuilder();
        String poolSettingsFilePath = builder
                .append(baseDirectory)
                .append("/conf/")
                .append("pool_settings.json")
                .toString();

        File file = new File(poolSettingsFilePath);

        if (!file.exists()) {
            throw new IllegalArgumentException("Cannot find pool settings file");
        }

        ObjectMapper mapper = new ObjectMapper();
        PoolSettings ps = null;
        try {
            ps = mapper.readValue(file, PoolSettings.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        CommonPool.init(ps);
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
