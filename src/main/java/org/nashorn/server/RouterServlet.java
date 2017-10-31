package org.nashorn.server;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import java.io.PrintWriter;
import java.io.Reader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.rabbitmq.client.*;
import org.apache.log4j.Logger;

@WebServlet(
        name = "routerServlet",
        urlPatterns = {"/*"},
        loadOnStartup = 1,
        asyncSupported = true
)
public class RouterServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(RouterServlet.class);

    private static final String ROUTING_KEY_SEPARATOR = "::";
    private static final String EXCHANGE_NAME = "router";
    private static final String RESPONSE_QUEUE_NAME = "router-response";

    private ConnectionFactory factory;

    private final AtomicLong counter = new AtomicLong(1);

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        factory = (ConnectionFactory) context.getAttribute("RABBITMQ_FACTORY");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String dispatchPath = "";
        LOGGER.info("START ASYNC");

        AsyncContext ac = req.startAsync(req, resp);
        String uri = req.getRequestURI();
        String apiType = (String) req.getAttribute("API_TYPE");
        LOGGER.info("Requested URI: " + uri);

        if (apiType.equals("block")) {
            dispatchPath = "/blockProc";
        }

        if (apiType.equals("async")) {
            dispatchPath = "/asyncProc";
        }

        if (dispatchPath.isEmpty()) {
            throw new ServletException();
        }

        LOGGER.info("Dispathc to: " + dispatchPath);
        ac.dispatch(dispatchPath);


        /*LOGGER.info("START SERVICE");

        final AsyncContext actx = req.startAsync();

        actx.start(() -> {

            LOGGER.info("START ASYNC EXECUTION");

            final long corrId = counter.getAndIncrement();

            final BlockingQueue<String> buffer = new ArrayBlockingQueue<String>(1);

            Connection conn = null;
            Channel channel = null;
            try {
                conn = factory.newConnection();

                *//* Create channel, declare Exchange and response queue *//*
                LOGGER.debug("Create channel, declare Exchange and response queue");
                channel = conn.createChannel();
                channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
                channel.queueDeclare(RESPONSE_QUEUE_NAME, false, false, false, null);

                final Consumer consumer = new DefaultConsumer(channel) {

                    @Override
                    public void handleDelivery(String consumerTag,
                                               Envelope envelope,
                                               AMQP.BasicProperties properties,
                                               byte[] body) throws IOException {

                        LOGGER.info("Handle delivery");

                        long respCorrId = Integer.parseInt(properties.getCorrelationId());

                            *//*
                                Process the response if corrIds are the same
                             *//*
                        LOGGER.info("Checking Correlation IDs");
                        LOGGER.debug("Request ID | Response ID = " + corrId + " | " + respCorrId);
                        if (respCorrId == corrId) {
                            LOGGER.info("CorrID are the same");
                            try {
                                LOGGER.info("Putting message");
                                buffer.put(new String(body, "UTF-8"));
                            } catch (InterruptedException ex) {
                                LOGGER.error("Error during putting a message", ex);
                            }
                        }
                    }
                };

                *//* Building routing key *//*
                String routingKey = buildRoutingKey(actx.getRequest());
                LOGGER.info(routingKey);

                *//* Build RequestObject from HttpServletRequest *//*
                RequestObject ro = buildRequestObject(corrId, (HttpServletRequest) actx.getRequest());

                byte[] bRequestObject = SerDesUtils.serialize(ro);

                *//* Properties for message *//*
                AMQP.BasicProperties props =
                        new AMQP.BasicProperties.Builder()
                                .correlationId(String.valueOf(ro.getId()))
                                .replyTo(RESPONSE_QUEUE_NAME)
                                .build();

                *//* Publish a message *//*
                LOGGER.info("Publish a message");
                channel.basicPublish(EXCHANGE_NAME, routingKey, props, bRequestObject);
                channel.basicConsume(RESPONSE_QUEUE_NAME, true, consumer);

                *//* Blocking waiting for a message *//*
                LOGGER.info("Waiting for a message");
                String message = null;
                try {
                    message = buffer.take();
                } catch (InterruptedException ex) {
                    LOGGER.error("Error during taking a message", ex);
                }

                    *//*
                        Building response
                     *//*
                LOGGER.info("Building response");
                HttpServletResponse httpResponse = (HttpServletResponse) actx.getResponse();
                try (final PrintWriter writer = httpResponse.getWriter()) {
                    if (writer.checkError()) {
                        throw new IOException();
                    }
                    writer.write(message);
                } catch (IOException ex) {
                    LOGGER.error("Error during writing to the client, ex");
                    httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }

                LOGGER.info("Commit response");
                actx.complete();

            } catch (IOException | TimeoutException ex) {
                *//* Log error message and create error message to the client *//*
                LOGGER.error("Some problem with Connection", ex);
                ex.printStackTrace();
            } finally {
                LOGGER.info("Closing Channel and Connection...");
                close(channel);
                close(conn);
            }
        });


        LOGGER.info("END SERVICE");
*/
    }

    private String buildRoutingKey(ServletRequest request) {
        StringBuilder sb = new StringBuilder();

        String apiVersion = (String) request.getAttribute("API_VERSION");
        String apiType = (String) request.getAttribute("API_TYPE");

        sb.append(apiVersion);
        sb.append(ROUTING_KEY_SEPARATOR);
        sb.append(apiType);

        return sb.toString();
    }

    private RequestObject buildRequestObject(long id, HttpServletRequest req) throws IOException {
        RequestObject.Builder builder = new RequestObject.Builder();

        builder.setId(id);
        builder.setUri(req.getRequestURI());
        builder.setMethod(req.getMethod());
        builder.setBody(getRequestBody(req));

        for (Enumeration<String> headerNames = req.getHeaderNames();
             headerNames.hasMoreElements(); ) {
            String headerName = headerNames.nextElement();
            builder.addHeader(headerName, req.getHeader(headerName));
        }

        return builder.build();
    }

    private String getRequestBody(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (final BufferedReader br = req.getReader()) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException ex) {
            LOGGER.error("Error during reading request body", ex);
            throw new IOException();
        }
        return sb.toString();
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
