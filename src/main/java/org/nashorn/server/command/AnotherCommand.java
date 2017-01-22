package org.nashorn.server.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AnotherCommand implements Command {

    private final String path = "nashorn/";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("ANOTHER COMMAND");
    }

    @Override
    public String getPath() {
        return path;
    }
}
