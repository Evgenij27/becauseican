package org.nashorn.server.command;


import org.nashorn.server.Bucket;
import org.nashorn.server.annotation.GetMapping;
import org.nashorn.server.db.DAO;
import org.nashorn.server.db.Memory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@GetMapping(path = "/nashorn/{id}")
public class GetCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = Long.parseLong((String)request.getAttribute("id"));

        DAO memory = Memory.INSTANCE;
        Bucket bucket = memory.read(id);

        String result = bucket.getOutput().getBuffer().toString();

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(result);

    }
}
