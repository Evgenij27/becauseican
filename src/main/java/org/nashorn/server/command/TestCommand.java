package org.nashorn.server.command;

import org.nashorn.server.annotation.POST;
import org.nashorn.server.annotation.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

@POST
@Path(path = "/nashorn/{id}/status")
public class TestCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    }
}
