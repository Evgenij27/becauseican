package org.nashorn.server.handler;

import org.apache.log4j.Logger;
import org.nashorn.server.util.resolver.CommandResolver;
import org.nashorn.server.util.resolver.CommandResolverImpl;
import org.nashorn.server.util.JsonSerDesEngine;
import org.nashorn.server.util.response.ScriptResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public abstract class AbstractHandler implements Handler {

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

    public String getRootPath() {
        return this.rootPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractHandler that = (AbstractHandler) o;

        return rootPath != null ? rootPath.equals(that.rootPath) : that.rootPath == null;
    }

    @Override
    public int hashCode() {
        return rootPath != null ? rootPath.hashCode() : 0;
    }

    @Override
    public int compareTo(Handler o) {
        AbstractHandler ah = (AbstractHandler) o;
        return ah.getRootPath().compareTo(getRootPath());
    }
}
