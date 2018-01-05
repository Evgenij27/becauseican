package org.nashorn.server.async.command;

import org.nashorn.server.Command;
import org.nashorn.server.util.JsonSerDesEngine;
import org.nashorn.server.util.PathVariableSupplier;
import org.nashorn.server.util.response.ScriptResponse;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.db.InMemoryStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CancelAndDeleteExecutionByIdAsyncCommand implements Command {
    @Override
    public Object execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        PathVariableSupplier pvs = new PathVariableSupplier(request);
        long id = pvs.supplyAsLong("id");

        ExecutionUnit unit = InMemoryStorage.instance().read(id);
        unit.cancel(true);
        InMemoryStorage.instance().delete(id);

        ScriptResponse.Builder respBuilder = new ScriptResponse.Builder();
        respBuilder.statusOK();
        respBuilder.copyHeadersFrom(response);
        respBuilder.noContent();
        respBuilder.withMessage("done!");

        try (final PrintWriter writer = response.getWriter()) {
            if (writer.checkError()) {
                throw new IOException("Client disconnected");
            }
            writer.write(JsonSerDesEngine.writeEntity(respBuilder.build()));
        } catch (IOException ex) {
            throw new ServletException(ex);
        }

        return respBuilder.build();
    }
}
