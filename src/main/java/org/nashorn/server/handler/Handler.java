package org.nashorn.server.handler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Handler {

    void handle(HttpServletRequest req, HttpServletResponse resp, HandlerChain chain)
            throws ServletException;
}
