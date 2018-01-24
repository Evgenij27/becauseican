package org.nashorn.server;

import org.apache.log4j.Logger;
import org.nashorn.server.command.block.SubmitNewScriptBlockCommand;
import org.nashorn.server.command.async.*;
import org.nashorn.server.handler.Handler;
import org.nashorn.server.handler.HandlerBuilder;
import org.nashorn.server.handler.HandlerChain;
import org.nashorn.server.handler.HandlerChainImpl;
import org.nashorn.server.handler.async.AsyncApiHandler;
import org.nashorn.server.handler.block.BlockApiHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        urlPatterns = {"/api/*"},
        name = "ApiServiceServlet",
        loadOnStartup = 1
)
public class ApiGatewayServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ApiGatewayServlet.class);

    private static final Handler HANDLER;
    private static final HandlerChain CHAIN;

    static {
        /*
         ==========================================================================
            Block API Handler and its Commands
         ==========================================================================
         */
       final HandlerBuilder blockBuilder = BlockApiHandler.newBuilder("/nashorn/api/v0.9/block");
         /*
            POST Endpoints
         */
        blockBuilder.postEndpoint("/script", new SubmitNewScriptBlockCommand());

        /*
        ============================================================================
            Async API Handler and its Commands
        ============================================================================
         */
        final HandlerBuilder asyncBuilder = AsyncApiHandler.newBuilder("/nashorn/api/v0.9/async");
        /*
            GET Endpoints
         */
        asyncBuilder.getEndpoint("/greetings/:name", new GreetingAsyncCommand());
        asyncBuilder.getEndpoint("/script/:id",      new GetScriptByIdAsyncCommand());
        asyncBuilder.getEndpoint("/script",          new GetAllScriptsAsyncCommand());
        /*
            POST Endpoints
         */
        asyncBuilder.postEndpoint("/script",         new SubmitNewScriptAsyncCommand());

        /*
            DELETE Endpoints
         */
        asyncBuilder.deleteEndpoint("/script/:id",     new CancelAndDeleteExecutionByIdAsyncCommand());

        HANDLER = blockBuilder.build();
        CHAIN = new HandlerChainImpl(asyncBuilder.build(), null);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        LOGGER.info("START SERVICE");

        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        try {
            HANDLER.handle(req, resp, CHAIN);
        } catch (ServletException ex) {
            LOGGER.error(ex);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("END SERVICE");
    }

}

