package org.nashorn.server.command;

import org.nashorn.server.annotation.DeleteMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.nashorn.server.db.DAO;
import org.nashorn.server.db.Memory;

@DeleteMapping(path = "/nashorn/{id}")
public class DeleteCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        long id = Long.parseLong( (String) request.getAttribute("id"));

        DAO memory = Memory.getInstance();

        memory.delete(id);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
