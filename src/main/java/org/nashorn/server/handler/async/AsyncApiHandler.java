package org.nashorn.server.handler.async;

import org.nashorn.server.command.Command;
import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.CommandNotFoundException;
import org.nashorn.server.handler.AbstractHandler;
import org.nashorn.server.handler.HandlerBuilder;
import org.nashorn.server.handler.HandlerChain;
import org.nashorn.server.util.response.ScriptResponse;
import org.nashorn.server.util.response.ResponseMessage;

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
    public void handle(HttpServletRequest req, HttpServletResponse resp, HandlerChain chain)
            throws ServletException {
        if (findMatch(rootPath, req.getRequestURI())) {
            try {
                Command command = resolverChain.resolve(req);
                Object msg = command.execute(req, resp);
                writeResponse(msg, resp);
            } catch (CommandNotFoundException | CommandExecutionException ex) {
                LOGGER.error(ex.getMessage());
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                ScriptResponse sr = buildErrorMsg(ex, resp);
                writeResponse(sr, resp);
            }
        } else if (chain != null) {
            chain.handle(req, resp);
        } else {
            writeResponse(new ResponseMessage("Bad request."), resp);
        }
    }
}
