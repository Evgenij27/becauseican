package org.nashorn.server;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class HttpResponseEntity extends HttpServletResponseWrapper {

    public HttpResponseEntity(HttpServletResponse response) {
        super(response);
    }
}
