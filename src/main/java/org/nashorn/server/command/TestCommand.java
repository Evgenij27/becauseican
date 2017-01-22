package org.nashorn.server.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

public class TestCommand implements Command {

    private final String path = "/nashorn/{id}";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        System.out.println("TEST COMMAND!!!!!");
    }

    @Override
    public String getPath() {
        return path;
    }
}
