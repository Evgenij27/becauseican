package org.nashorn.server.command;

import org.nashorn.server.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

@PostMapping(path = "/nashorn/")
public class TestCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        System.out.println("Hello POST!!!");
    }
}
