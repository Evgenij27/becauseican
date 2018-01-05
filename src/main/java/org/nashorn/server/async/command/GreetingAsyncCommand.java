package org.nashorn.server.async.command;

import org.nashorn.server.Command;
import org.nashorn.server.util.PathVariableSupplier;
import org.nashorn.server.util.response.ResponseMessage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GreetingAsyncCommand implements Command {

    @Override
    public Object execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        PathVariableSupplier pvs = new PathVariableSupplier(request);

        String name = pvs.supplyAsString("name");
        try (final PrintWriter writer = response.getWriter()) {
            if (writer.checkError()) throw new IOException("Client disconnection");
            writer.printf("Hello, %s%n", name);
        } catch (IOException ex) {
            throw new ServletException(ex);
        }
        ResponseMessage rm = new ResponseMessage();
        rm.setMessage(String.format("Hello, %s%n", name));
        return rm;
    }
}
