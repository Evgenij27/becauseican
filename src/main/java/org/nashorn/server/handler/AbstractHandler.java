package org.nashorn.server.handler;

import org.apache.log4j.Logger;
import org.nashorn.server.resolver.CommandResolver;
import org.nashorn.server.resolver.CommandResolverImpl;
import org.nashorn.server.util.JsonSerDesEngine;
import org.nashorn.server.util.response.ScriptResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class AbstractHandler implements Handler {

    private static final String PATH_REGEX = "(/([^/]+)(/)?)+?";

    protected static final Logger LOGGER = Logger.getLogger(AbstractHandler.class);

    protected final CommandResolver resolverChain;

    protected final String rootPath;

    protected AbstractHandler(HandlerBuilder builder) {
        this.resolverChain = buildResolverChain(builder);
        this.rootPath = builder.rootPath;
    }

    private CommandResolver buildResolverChain(HandlerBuilder b) {
        CommandResolver putResolver    = new CommandResolverImpl(b.putEndpoints, "PUT", null);
        CommandResolver deleteResolver = new CommandResolverImpl(b.deleteEndpoints, "DELETE", putResolver);
        CommandResolver postResolver   = new CommandResolverImpl(b.postEndpoints, "POST", deleteResolver);
        return new CommandResolverImpl(b.getEndpoints, "GET", postResolver);
    }

    protected void writeResponse(Object msg, HttpServletResponse resp) throws ServletException {
        try (final PrintWriter writer = resp.getWriter()) {
            if (writer.checkError()) {

                throw new IOException("Client disconnected");
            }
            writer.write(JsonSerDesEngine.writeEntity(msg));
        } catch (IOException ex) {
            LOGGER.error(ex);
            throw new ServletException(ex);
        }
    }

    protected ScriptResponse buildErrorMsg(Exception ex, HttpServletResponse resp) {

        return new ScriptResponse.Builder()
                .copyHeadersFrom(resp)
                .statusBadRequest()
                .noContent()
                .withMessage(ex.getMessage())
                .build();
    }

    private String transform(String rootPath) {
        return rootPath + PATH_REGEX;
    }

    protected boolean findMatch(String rootPath, String uri) {
        Pattern pattern = Pattern.compile(transform(rootPath));
        Matcher matcher = pattern.matcher(uri);
        return matcher.matches();
    }

    protected void throwServletException(String msg) throws ServletException {
        throw new ServletException(msg);
    }
}
