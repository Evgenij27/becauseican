package org.nashorn.server.command;

import org.nashorn.server.ResponseEntity;
import org.nashorn.server.db.Memory;
import org.nashorn.server.factory.TaskFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AnotherCommand implements Command {

    private final String path = "/nashorn/";

    @Override
    public ResponseEntity execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Memory memory = Memory.getInstance();
        TaskFactory factory = TaskFactory.getInstance();





        return new ResponseEntity.Builder().build();
    }

    @Override
    public String getPath() {
        return path;
    }
}
