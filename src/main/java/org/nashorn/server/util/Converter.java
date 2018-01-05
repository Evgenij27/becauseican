package org.nashorn.server.util;

public interface Converter<F, T> {

    T convert(F f);
}
