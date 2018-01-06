package org.nashorn.server;

import org.apache.log4j.Logger;
import org.nashorn.server.async.command.*;
import org.nashorn.server.block.command.SubmitNewScriptBlockCommand;
import org.nashorn.server.block.command.TestBlockCommand;
import org.nashorn.server.handler.HandlerBuilder;
import org.nashorn.server.handler.HandlerLookup;
import org.nashorn.server.handler.async.AsyncApiHandler;
import org.nashorn.server.handler.block.BlockApiHandler;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(
        urlPatterns = {"/api/*"},
        name = "ApiEntrypointServlet",
        loadOnStartup = 1
)
public class ApiEntrypointServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ApiEntrypointServlet.class);

    private final HandlerLookup lookup = new HandlerLookup();

    @Override
    public void init(ServletConfig config) throws ServletException {
         /*
         ==========================================================================
            Block API Handler and its Commands
         ==========================================================================
         */
        HandlerBuilder blockBuilder = BlockApiHandler.newBuilder("/nashorn/api/v0.9/block");
        blockBuilder.getEndpoint("/test", new TestBlockCommand());
         /*
            POST Endpoints
         */
        blockBuilder.postEndpoint("/script", new SubmitNewScriptBlockCommand());

        /*
        ============================================================================
            Async API Handler and its Commands
        ============================================================================
         */
        HandlerBuilder asyncBuilder = AsyncApiHandler.newBuilder("/nashorn/api/v0.9/async");
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



        lookup.registerHandler(blockBuilder.build());
        lookup.registerHandler(asyncBuilder.build());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        LOGGER.info("START SERVICE");

        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        try {
            lookup.lookupAndProcess(req, resp);
        } catch (ServletException ex) {
            LOGGER.error(ex);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("END SERVICE");
    }

}

