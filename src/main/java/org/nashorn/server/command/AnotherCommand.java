package org.nashorn.server.command;

import org.nashorn.server.annotation.GET;
import org.nashorn.server.annotation.Path;
import org.nashorn.server.db.Memory;
import org.nashorn.server.factory.TaskFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@GET
@Path(path = "/nashorn/")
public class AnotherCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Memory memory = Memory.getInstance();
        TaskFactory factory = TaskFactory.getInstance();
    }
}
