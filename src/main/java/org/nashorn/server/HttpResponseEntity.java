package org.nashorn.server;

import org.apache.log4j.Logger;
import org.nashorn.server.util.JsonSerDesEngine;
import org.nashorn.server.util.response.ScriptResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;

public class HttpResponseEntity extends HttpServletResponseWrapper {

    private static final Logger LOGGER = Logger.getLogger(HttpResponseEntity.class);

    public HttpResponseEntity(HttpServletResponse response) {
        super(response);
    }

    public void writeMessage(Object msg) throws ServletException {
        try (final PrintWriter writer = getWriter()) {
            if (writer.checkError()) {
                throw new IOException("Client disconnected");
            }
            writer.write(JsonSerDesEngine.writeEntity(msg));
        } catch (IOException ex) {
            LOGGER.error(ex);
            throw new ServletException(ex);
        }
    }

    public ScriptResponse writeErrorMessage(Exception ex) {
        return new ScriptResponse.Builder()
                .copyHeadersFrom(this)
                .statusBadRequest()
                .noContent()
                .withMessage(ex.getMessage())
                .build();
    }
}
