package org.nashorn.server;

import org.nashorn.server.util.PathVariableProcessingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class HttpRequestEntity extends HttpServletRequestWrapper {

    public HttpRequestEntity(HttpServletRequest request) {
        super(request);
    }

    private Object getOrThrow(String name) throws PathVariableProcessingException {
        Object attr = getRequest().getAttribute(name);
        if (attr == null) {
            throw new PathVariableProcessingException(String.format("Path variable %s not found", name));
        }
        return attr;
    }

    public String supplyAsString(String name) throws PathVariableProcessingException {
        return String.valueOf(getOrThrow(name));
    }

    public int supplyAsInt(String name) throws PathVariableProcessingException {
        try {
            return Integer.parseInt(supplyAsString(name));
        } catch (NumberFormatException ex) {
            throw new PathVariableProcessingException(ex.getMessage());
        }
    }

    public long supplyAsLong(String name) throws PathVariableProcessingException {
        try {
            return Long.parseLong(supplyAsString(name));
        } catch (NumberFormatException ex) {
            throw new PathVariableProcessingException(ex.getMessage());
        }
    }
}
