package org.nashorn.server.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

public class TestCommand implements Command {

    private final String path = "/nashorn/{id}/status";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        System.out.println(TestCommand.class.getSimpleName());
        System.out.println("ID -> " + request.getAttribute("id"));
        //System.out.println("STATUS -> " + request.getAttribute("status"));

    }

    @Override
    public String getPath() {
        return path;
    }
}
