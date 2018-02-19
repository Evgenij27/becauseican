package org.nashorn.server;

import org.apache.log4j.Logger;
import org.nashorn.server.command.async.CancelAndDeleteExecutionByIdAsyncCommand;
import org.nashorn.server.command.async.GetAllScriptsAsyncCommand;
import org.nashorn.server.command.async.GetScriptByIdAsyncCommand;
import org.nashorn.server.command.async.SubmitNewScriptAsyncCommand;
import org.nashorn.server.command.block.SubmitNewScriptBlockCommand;
import org.nashorn.server.handler.Handler;
import org.nashorn.server.handler.HandlerChain;
import org.nashorn.server.handler.HandlerChainImpl;
import org.nashorn.server.handler.RequestHandlerBuilder;

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
       final RequestHandlerBuilder blockBuilder = new RequestHandlerBuilder("/nashorn/api/v0.9/block");
         /*
            POST Endpoints
         */
        blockBuilder.postEndpoint("/script", new SubmitNewScriptBlockCommand());

        /*
        ============================================================================
            Async API Handler and its Commands
        ============================================================================
         */
        final RequestHandlerBuilder asyncBuilder = new RequestHandlerBuilder("/nashorn/api/v0.9/async");
        /*
            GET Endpoints
         */
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
            HttpRequestEntity reqEntity = new HttpRequestEntity(req);
            HttpResponsePublisher pub = new HttpResponsePublisher(resp);
            HANDLER.handle(reqEntity, pub, CHAIN);
        } catch (ServletException ex) {
            LOGGER.error(ex);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("END SERVICE");
    }
}