package org.nashorn.server.command;

import org.nashorn.server.annotation.DeleteMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@DeleteMapping(path = "/nashorn/")
public class DeleteCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Hello DELETE!!!");
    }
}
