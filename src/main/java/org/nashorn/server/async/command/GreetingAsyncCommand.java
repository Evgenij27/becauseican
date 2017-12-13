package org.nashorn.server.async.command;

import org.nashorn.server.AppException;
import org.nashorn.server.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GreetingAsyncCommand implements Command {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        String name = (String) req.getAttribute("name");
        try (final PrintWriter writer = resp.getWriter()) {
            if (writer.checkError()) throw new IOException("Client disconnection");
            writer.printf("Hello, %s\n", name);
        } catch (IOException ex) {
            throw new ServletException(ex);
        }

    }
}
