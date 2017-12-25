package org.nashorn.server.async.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nashorn.server.Command;
import org.nashorn.server.ScriptResponse;
import org.nashorn.server.ScriptResponseFactory;
import org.nashorn.server.StatusNames;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.db.InMemoryStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GetScriptByIdAsyncCommand implements Command {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String attr = (String) req.getAttribute("id");

        if (attr == null) {
            throw new ServletException("Bad request");
        }

        long id = Long.parseLong(attr);

        ExecutionUnit unit = InMemoryStorage.instance().read(id);

        String bresp = ScriptResponseFactory.newJsonResponseFor(unit);

        try (final PrintWriter writer = resp.getWriter()) {
            if (writer.checkError()) {
                throw new IOException("Client disconnected");
            }
            writer.print(bresp);
        } catch (IOException ex) {
            throw new ServletException(ex);
        }
    }
}
