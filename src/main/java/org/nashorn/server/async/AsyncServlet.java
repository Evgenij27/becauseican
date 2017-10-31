package org.nashorn.server.async;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

@WebServlet(
        name = "asyncServlet",
        urlPatterns = {"/asyncProc"},
        asyncSupported = true,
        loadOnStartup = 1
)
public class AsyncServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AsyncServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        LOGGER.info("START ASYNC");
        final AsyncContext ac = req.startAsync(req, resp);

        ac.start(() -> {

            try (PrintWriter writer = ac.getResponse().getWriter()) {
                for (int i = 0; i < 20; i++) {
                    if (writer.checkError()) {
                        throw new IOException();
                    }
                    writer.printf("%4d\n", i);
                    Thread.sleep(1000);
                }
            } catch (IOException ex) {
                LOGGER.error("IOException.....", ex);
            } catch (InterruptedException ex) {
                LOGGER.error("InterruptedException...." ,ex);
            }

            ac.complete();

        });



    }
}
