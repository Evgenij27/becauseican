package org.nashorn.server.command;


import org.nashorn.server.NashornWorker;
import org.nashorn.server.annotation.GetMapping;
import org.nashorn.server.db.DAO;
import org.nashorn.server.db.Memory;

import javax.script.ScriptContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.Future;



@GetMapping(path = "/nashorn/{id}")
public class GetCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        long id = Long.parseLong((String) request.getAttribute("id"));

        DAO memory = Memory.getInstance();
        Future<ScriptContext> deferedFuture = memory.read(id);

        NashornWorker worker = (NashornWorker) deferedFuture;

        StringWriter output = (StringWriter) worker.getOutputWriter();
        StringBuffer outputBuffer = output.getBuffer();

        PrintWriter writer = response.getWriter();
        writer.write(outputBuffer.toString());

        response.setStatus(HttpServletResponse.SC_OK);




    }
}
