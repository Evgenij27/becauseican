package org.nashorn.server.handler;

import org.apache.log4j.Logger;
import org.nashorn.server.CommandResolver;
import org.nashorn.server.util.JsonSerDesEngine;
import org.nashorn.server.util.response.ScriptResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public abstract class AbstractHandler implements Handler {

    private static final Logger LOGGER = Logger.getLogger(AbstractHandler.class);

    protected final CommandResolver resolver;

    protected final String rootPath;

    protected AbstractHandler(HandlerBuilder builder) {
        this.resolver = new CommandResolver(builder.getEndpoints, builder.postEndpoints,
                builder.putEndpoints, builder.deleteEndpoints);
        this.rootPath = builder.rootPath;
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

    protected ScriptResponse buildErrorMsg(Exception ex, HttpServletResponse resp) throws ServletException {
        ScriptResponse sr = new ScriptResponse.Builder()
                .copyHeadersFrom(resp)
                .statusError()
                .noContent()
                .withMessage(ex.getMessage())
                .build();
        return sr;
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
