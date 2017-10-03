package org.nashorn.server;


import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.concurrent.TimeoutException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.log4j.Logger;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

@WebServlet(
        name = "routerServlet",
        urlPatterns = {"/*"},
        loadOnStartup = 1
       )
public class RouterServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(RouterServlet.class);

    private static final String QUEUE_NAME = "hello";

    private Connection conn;
    private Channel channel;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        ConnectionFactory factory = (ConnectionFactory) context.getAttribute("RABBITMQ_FACTORY");

        try {
            conn = factory.newConnection();
        } catch (IOException |TimeoutException ex) {
            LOGGER.error("Error during creating connection", ex);
            throw new ServletException(ex);
        }

        try {
            channel = conn.createChannel();
        } catch (IOException ex) {
            LOGGER.error("Error during creating channel", ex);
            throw new ServletException(ex);
        }

    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        LOGGER.info("START SERVICE");

        String apiVersion = (String) req.getAttribute("API_VERSION");
        String apiType    = (String) req.getAttribute("API_TYPE");

        LOGGER.debug("API VERSION: " + apiVersion);
        LOGGER.debug("API TYPE   : " + apiType);

        String uriMessage = req.getRequestURI();

        LOGGER.info("Sending... " + uriMessage);
        channel.basicPublish("", QUEUE_NAME, null, uriMessage.getBytes());
    }

    @Override
    public void destroy() {
        close(channel);
        close(conn);
    }

    private void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (IOException ex) {
                LOGGER.error("Cannot close connection", ex);
            }
        }
    }

    private void close(Channel channel) {
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException ex) {
                LOGGER.error("Cannot close channel", ex);
            } catch (TimeoutException tex) {
                LOGGER.error("Timeout has expired during closing the channel", tex);
            }
        }
    }

    private ScriptEntity getScriptEntity(HttpServletRequest req) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(req.getReader(), ScriptEntity.class);
    }
}