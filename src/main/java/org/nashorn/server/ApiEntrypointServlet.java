package org.nashorn.server;

import org.apache.log4j.Logger;
import org.nashorn.server.async.command.GreetingAsyncCommand;
import org.nashorn.server.block.command.SubmitNewScriptCommand;
import org.nashorn.server.block.command.TestBlockCommand;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(
        urlPatterns = {"/api/*"},
        asyncSupported = true,
        name = "ApiEntrypointServlet"
)
public class ApiEntrypointServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ApiEntrypointServlet.class);

    private final HandlerLookup lookup = new HandlerLookup();

    @Override
    public void init(ServletConfig config) throws ServletException {
        LOGGER.info("INIT");

        /*
            Block API Handler and its Commands
         */
        ApiHandler blockApiHandler =
                new ApiHandler.Builder("/nashorn/api/v0.9/block")
        .registerGetEndpoint("/test", new TestBlockCommand())
        .registerPostEndpoint("/script", new SubmitNewScriptCommand())
        .build();


        /*
            Async API Handler and its Commands
         */
        ApiHandler asyncApiHandler =
                new ApiHandler.Builder("/nashorn/api/v0.9/async")
        .registerGetEndpoint("/greetings/:name", new GreetingAsyncCommand())
        .build();

        lookup.registerHandler(blockApiHandler);
        lookup.registerHandler(asyncApiHandler);

        LOGGER.info("END INIT");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        LOGGER.info("SERVICE");
        final AsyncContext context = req.startAsync();
        context.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                LOGGER.info("onComplete");
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {

            }

            @Override
            public void onError(AsyncEvent event) throws IOException {
                LOGGER.error("onError " + event.getThrowable());
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {

            }
        });
        context.start(() -> {
            try {
                lookup.lookupAndProcess(req, resp);
            } catch (IOException e) {
                LOGGER.error(e);
            } catch (ServletException e) {
                LOGGER.error(e);
            }
            context.complete();
        });
        LOGGER.info("END SERVICE");
    }




}
