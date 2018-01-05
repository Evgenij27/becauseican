package org.nashorn.server.handler.async;

import org.apache.log4j.Logger;
import org.nashorn.server.Command;
import org.nashorn.server.handler.AbstractHandler;
import org.nashorn.server.handler.HandlerBuilder;
import org.nashorn.server.util.JsonSerDesEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AsyncApiHandler extends AbstractHandler {

    private static final Logger LOGGER = Logger.getLogger(AsyncApiHandler.class);

    public static HandlerBuilder newBuilder(String root) {
        return new AsyncApiHandlerBuilder(root);
    }

    protected AsyncApiHandler(HandlerBuilder builder) {
        super(builder);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Command command = resolver.resolve(request);
        if (command == null) {
            throw new ServletException("Bad request");
        }

        Object resp = command.execute(request, response);
        String bresp = JsonSerDesEngine.writeEntity(resp);
        try (final PrintWriter writer = response.getWriter()) {
            if (writer.checkError()) {
                LOGGER.error("Client disconnected");
                throw new IOException("Client disconnected");
            }
            writer.write(bresp);
        } catch (IOException ex) {
            throw new ServletException(ex);
        }
    }
}
