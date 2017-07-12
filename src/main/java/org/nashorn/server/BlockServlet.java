package org.nashorn.server;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nashorn.server.core.NashornHandler;
import org.nashorn.server.core.NashornServiceFactory;

@WebServlet(name = "blockServlet", urlPatterns = {"/block"}, loadOnStartup = 1)
public class BlockServlet extends HttpServlet {

    private final ExecutorService executor = Executors.newFixedThreadPool(3);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        //resp.setContentType("text/plain;charset=UTF-8");

        NashornServiceFactory factory = NashornServiceFactory.newFactory();
        factory.setCommonExecutor(executor);

        NashornHandler handler = new NashornHandler();
        handler.handle(req, resp, factory);

    }

    private ScriptEntity getScriptEntity(HttpServletRequest req) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(req.getReader(), ScriptEntity.class);
    }

}
