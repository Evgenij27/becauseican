package org.nashorn.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();
        Set<String> resources = context.getResourcePaths("/WEB-INF/classes/");
        resources.stream().forEach((el) -> {System.out.println(el);});

        InputStream is = context.getResourceAsStream("/WEB-INF/classes/pool_settings.json");

        if (is == null) {
            System.out.println("Cannot find pool settings...");
            throw new IllegalArgumentException("Cannot find pool settings...");
        }

        byte[] buffer = null;
        try {
            int available = is.available();
            buffer = new byte[available];
            is.read(buffer);
            is.reset();
            System.out.println(new String(buffer, "ISO-8859-1"));
        } catch (IOException ex) {}

        ObjectMapper mapper = new ObjectMapper();
        PoolSettings ps = null;
        try {
            ps = mapper.readValue(is, PoolSettings.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println(ps);

        CommonPool.init(ps);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("SHUTDOWN");
        CommonPool.getInstance().shutdown();
    }
}
