package org.nashorn.server;

import org.apache.log4j.Logger;
import org.nashorn.server.async.command.GreetingAsyncCommand;
import org.nashorn.server.block.command.TestBlockCommand;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentSkipListMap;
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

    private static final String SEPARATOR = "::";

    private final ConcurrentSkipListSet<Handler> handlers = new ConcurrentSkipListSet<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        LOGGER.info("INIT");
        ApiHandler.Builder bb = new ApiHandler.Builder("/nashorn/api/v0.9/block");
        bb.registerGetEndpoint("/test", new TestBlockCommand());
        handlers.add(bb.build());

        ApiHandler.Builder ab = new ApiHandler.Builder("/nashorn/api/v0.9/async");
        ab.registerGetEndpoint("/greetings/:name", new GreetingAsyncCommand());
        handlers.add(ab.build());
        LOGGER.info("END INIT");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        LOGGER.info("SERVICE");
        String uri = req.getRequestURI();
        Handler handler = null;
        PrintWriter writer = resp.getWriter();
        for (Handler h : handlers) {
            ApiHandler ah = (ApiHandler) h;
            writer.printf("Reqeusted URI is %s => Transformed to %s is matches %s\n", req.getRequestURI(),
                    transform(ah.getRootPath()), pathMatcher(ah.getRootPath(), req.getRequestURI()));
           if (pathMatcher(ah.getRootPath(), uri)) {
               handler = ah;
               break;
           }
        }

        if (handler != null) {
            try {
                handler.handle(req, resp);
            } catch (AppException e) {
                LOGGER.error(e.getMessage());
            }
        }

        LOGGER.info("END SERVICE");
    }

    private String transform(String path) {
        return path + "(/([^/]+)(/)?)+?";
    }

    private boolean pathMatcher(String path, String uri) {
        Pattern pattern = Pattern.compile(transform(path));
        Matcher matcher = pattern.matcher(uri);
        return matcher.matches();
    }
}
