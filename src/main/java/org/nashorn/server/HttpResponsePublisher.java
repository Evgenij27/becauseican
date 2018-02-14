package org.nashorn.server;

import org.nashorn.server.util.JsonSerDesEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HttpResponsePublisher {

    private final HttpServletResponse resp;

    private Object body;

    public HttpResponsePublisher(HttpServletResponse resp) {
        this.resp = resp;
    }

    private void writeMessage(Object msg) throws ServletException {
        try (final PrintWriter writer = resp.getWriter()) {
            if (writer.checkError()) {
                throw new IOException("Client disconnected");
            }
            writer.write(JsonSerDesEngine.writeEntity(msg));
        } catch (IOException ex) {
            throw new ServletException(ex);
        }
    }

    public HttpResponsePublisher addHeader(String name, String value) {
        resp.addHeader(name, value);
        return this;
    }

    public HttpResponsePublisher statusOK() {
        resp.setStatus(HttpServletResponse.SC_OK);
        return this;
    }

    public HttpResponsePublisher statusCreated() {
        resp.setStatus(HttpServletResponse.SC_CREATED);
        return this;
    }

    public HttpResponsePublisher statusNoContent() {
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        body = null;
        return this;
    }

    public HttpResponsePublisher statusBadRequest() {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        body = null;
        return this;
    }

    public HttpResponsePublisher content(Object body) {
        this.body = body;
        return this;
    }

    public HttpResponsePublisher noContent() {
        this.body = null;
        return this;
    }

    public void publish() throws ServletException {
        writeMessage(body);
    }


}
