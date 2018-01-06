package org.nashorn.server.handler.block;

import org.nashorn.server.Command;
import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.CommandNotFoundException;
import org.nashorn.server.handler.AbstractHandler;
import org.nashorn.server.handler.HandlerBuilder;
import org.nashorn.server.util.response.ScriptResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BlockApiHandler extends AbstractHandler {

    public static HandlerBuilder newBuilder(String root) {
        return new BlockApiHandlerBuilder(root);
    }

    protected BlockApiHandler(HandlerBuilder builder) {
        super(builder);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        try {
            Command command = resolverChain.resolve(request);
            command.execute(request, response);
        } catch (CommandNotFoundException | CommandExecutionException ex) {
            LOGGER.error(ex);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ScriptResponse sr = buildErrorMsg(ex, response);
            writeResponse(sr, response);
        }
    }
}
