package org.nashorn.server;

import org.apache.log4j.Logger;
import org.nashorn.server.async.command.GetScriptByIdAsyncCommand;
import org.nashorn.server.async.command.GreetingAsyncCommand;
import org.nashorn.server.async.command.SubmitNewScriptAsyncCommand;
import org.nashorn.server.block.command.SubmitNewScriptBlockCommand;
import org.nashorn.server.block.command.TestBlockCommand;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(
        urlPatterns = {"/api/*"},
        asyncSupported = true,
        name = "ApiEntrypointServlet",
        loadOnStartup = 1
)
public class ApiEntrypointServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ApiEntrypointServlet.class);

    private final HandlerLookup lookup = new HandlerLookup();

    @Override
    public void init(ServletConfig config) throws ServletException {
         /*
            Block API Handler and its Commands
         */
        final ApiHandler.Builder blockBuilder = new ApiHandler.Builder("/nashorn/api/v0.9/block");
        blockBuilder.registerGetEndpoint("/test", new TestBlockCommand());
        blockBuilder.registerPostEndpoint("/script", new SubmitNewScriptBlockCommand());

        /*
            Async API Handler and its Commands
         */
        final ApiHandler.Builder asyncBuilder = new ApiHandler.Builder("/nashorn/api/v0.9/async");
        /*
            GET Endpoints
         */
        asyncBuilder.registerGetEndpoint("/greetings/:name", new GreetingAsyncCommand());
        asyncBuilder.registerGetEndpoint("/script/:id", new GetScriptByIdAsyncCommand());
        /*
            POST Endpoints
         */
        asyncBuilder.registerPostEndpoint("/script", new SubmitNewScriptAsyncCommand());

        lookup.registerHandler(blockBuilder.build());
        lookup.registerHandler(asyncBuilder.build());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        LOGGER.info("SERVICE");

        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        final AsyncContext context = req.startAsync();

        context.start(() -> {
            try {
                lookup.lookupAndProcess(req, resp);
            } catch (ServletException ex) {
                LOGGER.error(ex);
                HttpServletResponse httpResp = (HttpServletResponse) context.getResponse();
                httpResp.setStatus(400);
            } catch (IOException ex) {
                LOGGER.error(ex);
                HttpServletResponse httpResp = (HttpServletResponse) context.getResponse();
                httpResp.setStatus(500);
            }
            context.complete();
        });
        LOGGER.info("END SERVICE");


    }
}
