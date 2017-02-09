package org.nashorn.server;


import org.nashorn.server.command.Command;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.*;


@WebServlet(name = "home", urlPatterns = {"/*"}, loadOnStartup = 1, asyncSupported = true)
public class HomeServlet extends HttpServlet {

    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    private static final CommandResolver resolver = new CommandResolver(CommandRegistry.getRegistry());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Command com = resolver.resolve(req, resp);
        System.out.println(com);
        com.execute(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Command com = resolver.resolve(req, resp);
        ResponseEntity re = com.execute(req, resp);
        resp.setStatus(re.getStatus());
        Map<String, String> headers = re.getHeaders();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            resp.setHeader(entry.getKey(), entry.getValue());
        }
        PrintWriter writer = resp.getWriter();
        writer.print(re.getBody());
    }
}
