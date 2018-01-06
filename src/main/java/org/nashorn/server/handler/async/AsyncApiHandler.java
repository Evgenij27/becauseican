package org.nashorn.server.handler.async;

import org.nashorn.server.Command;
import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.CommandNotFoundException;
import org.nashorn.server.handler.AbstractHandler;
import org.nashorn.server.handler.HandlerBuilder;
import org.nashorn.server.util.response.ScriptResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AsyncApiHandler extends AbstractHandler {

    public static HandlerBuilder newBuilder(String root) {
        return new AsyncApiHandlerBuilder(root);
    }

    protected AsyncApiHandler(HandlerBuilder builder) {
        super(builder);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            Command command = resolverChain.resolve(request);
            Object msg = command.execute(request, response);
            writeResponse(msg, response);
        } catch (CommandNotFoundException | CommandExecutionException ex) {
            LOGGER.error(ex.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ScriptResponse sr = buildErrorMsg(ex, response);
            writeResponse(sr, response);
        }
    }
}
