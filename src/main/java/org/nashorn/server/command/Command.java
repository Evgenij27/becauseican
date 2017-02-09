package org.nashorn.server.command;

import org.nashorn.server.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

public interface Command {

    ResponseEntity execute(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException;

    String getPath();
}
