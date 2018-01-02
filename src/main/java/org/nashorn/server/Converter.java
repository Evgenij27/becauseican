package org.nashorn.server;

public interface Converter<F, T> {

    T convert(F f);
}
