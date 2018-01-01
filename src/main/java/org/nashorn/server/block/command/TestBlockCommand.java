package org.nashorn.server.block.command;

import org.nashorn.server.AppException;
import org.nashorn.server.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TestBlockCommand implements Command {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        try (final PrintWriter writer = resp.getWriter()) {
            for (int i = 0; i < 20; i++) {
                if (writer.checkError()) {
                    throw new IOException("Client disconnected");
                }
                writer.printf("%4d\n", i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new ServletException(e);
                }
            }
        } catch (IOException ex) {
            throw new ServletException(ex);
        }
    }
}
