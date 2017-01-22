package org.nashorn.server;

import org.nashorn.server.command.AnotherCommand;
import org.nashorn.server.command.Command;
import org.nashorn.server.command.TestCommand;
import org.nashorn.server.factory.ScriptTask;
import org.nashorn.server.factory.TaskFactory;

import javax.script.ScriptException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.*;


@WebServlet(name = "home", urlPatterns = {"/*"}, loadOnStartup = 1, asyncSupported = true)
public class HomeServlet extends HttpServlet {

    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    private static final CommandResolver resolver = new CommandResolver();

    static {
        CommandResolver.registerPost(new TestCommand());
        CommandResolver.registerPost(new AnotherCommand());
    }

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
        com.execute(req, resp);
    }

    private String getCode(HttpServletRequest req) {
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        String data = null;
        try {
            br = req.getReader();
            while ((data = br.readLine()) != null) {
                sb.append(data);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }
}
