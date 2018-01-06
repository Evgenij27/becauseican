package org.nashorn.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Command {

    Object execute(HttpServletRequest request, HttpServletResponse response)
            throws CommandExecutionException, ServletException;
}
