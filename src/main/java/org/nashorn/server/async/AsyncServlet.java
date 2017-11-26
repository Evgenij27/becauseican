package org.nashorn.server.async;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.nashorn.server.Command;
import org.nashorn.server.ScriptEntity;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(
        name = "asyncServlet",
        urlPatterns = {"/async/*"},
        loadOnStartup = 1,
        asyncSupported = true
)
public class AsyncServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AsyncServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        LOGGER.info("START ASYNC");
        final AsyncContext actx = req.startAsync();
        actx.start(() -> {
            HttpServletRequest httpRequest   = ((HttpServletRequest) actx.getRequest());
            HttpServletResponse httpResponse = ((HttpServletResponse) actx.getResponse());
            Command command = (Command) httpRequest.getAttribute("COMMAND");
            try {
                command.execute(httpRequest, httpResponse);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServletException e) {
                e.printStackTrace();
            }
            actx.complete();
        });
        LOGGER.info("END SERVICE");
    }


    private String getRequestBody(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (final BufferedReader br = req.getReader()) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException ex) {
            LOGGER.error("Error during reading request body", ex);
            throw new IOException();
        }
        return sb.toString();
    }

    private ScriptEntity getScriptEntity(HttpServletRequest req) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(req.getReader(), ScriptEntity.class);
    }
}
