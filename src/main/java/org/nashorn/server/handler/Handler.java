package org.nashorn.server.handler;

import org.nashorn.server.HttpRequestEntity;
import org.nashorn.server.HttpResponseEntity;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Handler {

    void handle(HttpRequestEntity req, HttpResponseEntity resp, HandlerChain chain)
            throws ServletException;
}
