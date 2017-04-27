package org.nashorn.server.command;

import org.nashorn.server.annotation.GetMapping;
import org.nashorn.server.core.Result;
import org.nashorn.server.db.DAO;
import org.nashorn.server.db.Memory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@GetMapping(path = "/nashorn/{id}")
public class GetResultCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        long id = Long.parseLong((String) request.getAttribute("id"));

        DAO memory = Memory.getInstance();

        Future<Result> result = memory.read(id);

        if (!result.isDone()) {
             try (final PrintWriter writer = response.getWriter()) {
                 if (writer.checkError()) {
                     throw new ServletException();
                 }
                 writer.print("working : true");
             }
             return;
        }

        StringWriter resultWriter = null;

        try {
            resultWriter = (StringWriter) result.get().getResult();
            try (final PrintWriter writer = response.getWriter()) {
                if (writer.checkError()) {
                    throw new ServletException();
                }
                System.out.println("RESULT ====> " + resultWriter.getBuffer().toString());
                writer.print(resultWriter.getBuffer().toString());
            }
        } catch (InterruptedException e) {
            throw new ServletException(e);
        } catch (ExecutionException e) {
            throw new ServletException(e);
        }
    }
}
