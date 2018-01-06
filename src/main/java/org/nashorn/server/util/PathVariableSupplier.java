package org.nashorn.server.util;

import org.nashorn.server.PathVariableNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

public class PathVariableSupplier {

    private final HttpServletRequest request;

    public PathVariableSupplier(HttpServletRequest request) {
        this.request = request;
    }

    private Object getOrThrow(String name) throws PathVariableNotFoundException {
        Object attr = request.getAttribute(name);
        if (attr == null) {
            throw new PathVariableNotFoundException(String.format("Path variable %s not found", name));
        }
        return attr;
    }

    public String supplyAsString(String name) throws PathVariableNotFoundException {
        Object attr = getOrThrow(name);
        Converter<Object, String> converter = String::valueOf;
        return converter.convert(attr);
    }

    public int supplyAsInt(String name) throws PathVariableNotFoundException {
        String attr = supplyAsString(name);
        Converter<String, Integer> converter = Integer::parseInt;
        return converter.convert(attr);
    }

    public long supplyAsLong(String name) throws PathVariableNotFoundException {
        String attr = supplyAsString(name);
        Converter<String, Long> converter = Long::parseLong;
        return converter.convert(attr);
    }
}