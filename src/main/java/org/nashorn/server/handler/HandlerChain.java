package org.nashorn.server.handler;

import org.nashorn.server.HttpRequestEntity;
import org.nashorn.server.HttpResponsePublisher;

import javax.servlet.ServletException;

public interface HandlerChain {

    void handle(HttpRequestEntity req, HttpResponsePublisher pub) throws ServletException;
}
