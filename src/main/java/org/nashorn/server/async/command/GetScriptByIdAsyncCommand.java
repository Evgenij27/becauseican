package org.nashorn.server.async.command;

import org.apache.log4j.Logger;
import org.nashorn.server.Command;
import org.nashorn.server.PathVariableSupplier;
import org.nashorn.server.ScriptResponseFactory;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.db.InMemoryStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GetScriptByIdAsyncCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(GetScriptByIdAsyncCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        PathVariableSupplier pvs = new PathVariableSupplier(request);

        long id = pvs.supplyAsLong("id");

        ExecutionUnit unit = InMemoryStorage.instance().read(id);

        String bresp = ScriptResponseFactory.newJsonResponseFor(unit);

        try (final PrintWriter writer = response.getWriter()) {
            if (writer.checkError()) {
                LOGGER.error("Client disconnected");
                throw new IOException("Client disconnected");
            }
            writer.print(bresp);
        } catch (IOException ex) {
            throw new ServletException(ex);
        }
    }
}
