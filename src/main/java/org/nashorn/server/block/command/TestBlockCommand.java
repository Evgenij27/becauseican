package org.nashorn.server.block.command;

import org.nashorn.server.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TestBlockCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        try (final PrintWriter writer = response.getWriter()) {
            for (int i = 0; i < 20; i++) {
                if (writer.checkError()) {
                    throw new IOException("Client disconnected");
                }
                writer.printf("%4d%n", i);
                sleep(1000);
            }
        } catch (IOException ex) {
            throw new ServletException(ex);
        }
    }

    private void sleep(long millis) throws ServletException {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ServletException(e);
        }
    }
}
