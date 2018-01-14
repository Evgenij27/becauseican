package org.nashorn.server.util;

import javax.servlet.http.HttpServletRequest;

public class PathVariableSupplier {

    private final HttpServletRequest request;

    public PathVariableSupplier(HttpServletRequest request) {
        this.request = request;
    }

    private Object getOrThrow(String name) throws PathVariableProcessingException {
        Object attr = request.getAttribute(name);
        if (attr == null) {
            throw new PathVariableProcessingException(String.format("Path variable %s not found", name));
        }
        return attr;
    }

    public String supplyAsString(String name) throws PathVariableProcessingException {
        Converter<Object, String> converter = String::valueOf;
        return converter.convert(getOrThrow(name));
    }

    public int supplyAsInt(String name) throws PathVariableProcessingException {
        try {
            String attr = supplyAsString(name);
            Converter<String, Integer> converter = Integer::parseInt;
            return converter.convert(attr);
        } catch (NumberFormatException ex) {
            throw new PathVariableProcessingException(ex.getMessage());
        }
    }

    public long supplyAsLong(String name) throws PathVariableProcessingException {
        try {
            String attr = supplyAsString(name);
            Converter<String, Long> converter = Long::parseLong;
            return converter.convert(attr);
        } catch (NumberFormatException ex) {
            throw new PathVariableProcessingException(ex.getMessage());
        }
    }
}