package org.nashorn.server.handler.async;

import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.CommandNotFoundException;
import org.nashorn.server.HttpRequestEntity;
import org.nashorn.server.HttpResponsePublisher;
import org.nashorn.server.command.Command;
import org.nashorn.server.handler.AbstractHandler;
import org.nashorn.server.handler.HandlerBuilder;
import org.nashorn.server.handler.HandlerChain;

import javax.servlet.ServletException;

public class AsyncApiHandler extends AbstractHandler {

    public static HandlerBuilder newBuilder(String root) {
        return new AsyncApiHandlerBuilder(root);
    }

    protected AsyncApiHandler(HandlerBuilder builder) {
        super(builder);
    }

    @Override
    public void handle(HttpRequestEntity req, HttpResponsePublisher pub, HandlerChain chain)
            throws ServletException {
        if (findMatch(rootPath, req.getRequestURI())) {
            try {
                Command command = resolverChain.resolve(req);
                command.execute(req, pub);
            } catch (CommandNotFoundException | CommandExecutionException ex) {
                LOGGER.error(ex.getMessage());
                pub.statusBadRequest().content(ex.getMessage()).publish();

            }
        } else if (chain != null) {
            chain.handle(req, pub);
        } else {
            pub.statusBadRequest()
                    .content("Bad request. Such type of API is not supported.")
                    .publish();
        }
    }
}
