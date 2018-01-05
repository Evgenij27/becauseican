package org.nashorn.server.util;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

public class PathVariableSupplier {

    private final HttpServletRequest request;

    public PathVariableSupplier(HttpServletRequest request) {
        this.request = request;
    }

    private Object getOrThrow(String name) throws NoSuchElementException {
        Object attr = request.getAttribute(name);
        if (attr == null) {
            throw new NoSuchElementException();
        }
        return attr;
    }

    public String supplyAsString(String name) throws NoSuchElementException {
        Object attr = getOrThrow(name);
        Converter<Object, String> converter = String::valueOf;
        return converter.convert(attr);
    }

    public int supplyAsInt(String name) throws NoSuchElementException {
        String attr = supplyAsString(name);
        Converter<String, Integer> converter = Integer::parseInt;
        return converter.convert(attr);
    }

    public long supplyAsLong(String name) throws NoSuchElementException {
        String attr = supplyAsString(name);
        Converter<String, Long> converter = Long::parseLong;
        return converter.convert(attr);
    }



}
