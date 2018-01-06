package org.nashorn.server.handler;

import org.nashorn.server.CommandNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Handler extends Comparable<Handler> {

    void handle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException;
}
