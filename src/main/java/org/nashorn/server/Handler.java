package org.nashorn.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Handler extends Comparable<Handler> {

    void handle(HttpServletRequest request, HttpServletResponse response)
            throws AppException;
}
