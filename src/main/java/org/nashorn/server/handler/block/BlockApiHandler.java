package org.nashorn.server.handler.block;

import org.nashorn.server.Command;
import org.nashorn.server.handler.AbstractHandler;
import org.nashorn.server.handler.HandlerBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BlockApiHandler extends AbstractHandler {

    public static HandlerBuilder newBuilder(String root) {
        return new BlockApiHandlerBuilder(root);
    }

    protected BlockApiHandler(HandlerBuilder builder) {
        super(builder);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Command command = resolver.resolve(request);
        if (command == null) {
            throw new ServletException("Bad request");
        }
        command.execute(request, response);
    }

}
