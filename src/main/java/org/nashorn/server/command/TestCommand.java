package org.nashorn.server.command;

import org.nashorn.server.ResponseEntity;
import org.nashorn.server.data.Body;
import org.nashorn.server.db.Memory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;

public class TestCommand implements Command {

    private final String path = "/nashorn/{id}/status";

    @Override
    public ResponseEntity execute(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        System.out.println(TestCommand.class.getSimpleName());
        System.out.println("ID -> " + request.getAttribute("id"));
        //System.out.println("STATUS -> " + request.getAttribute("status"));


        return new ResponseEntity.Builder().build();

    }

    @Override
    public String getPath() {
        return path;
    }
}
