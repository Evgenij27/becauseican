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

    private final CommandResolver resolver = new CommandResolver(CommandRegistry.getRegistry());

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Command command = resolver.resolve(req, resp);
        command.execute(req, resp);
    }
}
