package org.nashorn.server;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandlerLookup {

    private static final Logger LOGGER = Logger.getLogger(HandlerLookup.class);

    private static final String PATH_REGEX = "(/([^/]+)(/)?)+?";

    private final ConcurrentSkipListSet<Handler> handlers = new ConcurrentSkipListSet<>();

    public void registerHandler(Handler h) {
        LOGGER.info(String.format("Register Handler : %s", h));
        handlers.add(h);
    }

    public void lookupAndProcess(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        LOGGER.info("Start Lookup");

        final String uri = request.getRequestURI();

        LOGGER.info(String.format("Requested URI : %s", uri));

        Optional<Handler> oHandler = handlers.stream().filter( h -> {
            ApiHandler ah = (ApiHandler) h;
            return pathMatcher(ah.getRootPath(), uri);
        }).findFirst();

        Handler handler = oHandler.orElseThrow(ServletException::new);
        LOGGER.info("Handle request");
        handler.handle(request, response);
    }

    private String transform(String path) {
        return path + PATH_REGEX;
    }

    private boolean pathMatcher(String path, String uri) {
        Pattern pattern = Pattern.compile(transform(path));
        Matcher matcher = pattern.matcher(uri);
        return matcher.matches();
    }
}
