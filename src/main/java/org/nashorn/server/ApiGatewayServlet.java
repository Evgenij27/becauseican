package org.nashorn.server;

import org.apache.log4j.Logger;
import org.nashorn.server.handler.HandlerChain;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApiGatewayServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ApiGatewayServlet.class);

    private final HandlerChain handlerChain;

    public ApiGatewayServlet(HandlerChain handlerChain) {
        this.handlerChain = handlerChain;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        LOGGER.info("START SERVICE");

        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        try {
            HttpRequestEntity reqEntity = new HttpRequestEntity(req);
            HttpResponsePublisher pub = new HttpResponsePublisher(resp);
            handlerChain.handle(reqEntity, pub);
        } catch (ServletException ex) {
            LOGGER.error(ex);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("END SERVICE");
    }
}