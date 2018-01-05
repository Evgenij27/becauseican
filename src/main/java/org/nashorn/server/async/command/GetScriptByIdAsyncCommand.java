package org.nashorn.server.async.command;

import org.apache.log4j.Logger;
import org.nashorn.server.*;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.db.InMemoryStorage;
import org.nashorn.server.util.JsonSerDesEngine;
import org.nashorn.server.util.response.Href;
import org.nashorn.server.util.PathVariableSupplier;
import org.nashorn.server.util.response.ScriptContent;
import org.nashorn.server.util.response.ScriptResponse;
import org.nashorn.server.util.response.ScriptUnitData;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GetScriptByIdAsyncCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(GetScriptByIdAsyncCommand.class);

    @Override
    public Object execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        PathVariableSupplier pvs = new PathVariableSupplier(request);

        long id = pvs.supplyAsLong("id");

        ExecutionUnit unit = InMemoryStorage.instance().read(id);

        ScriptResponse.Builder builder = new ScriptResponse.Builder();

        builder.statusOK();
        builder.copyHeadersFrom(response);

        ScriptContent sc = new ScriptContent();
        sc.setScript(new ScriptUnitData(unit));
        sc.setHref(Href.newBuilder(request.getRequestURL()).build());

        builder.addContent(sc);

        try (final PrintWriter writer = response.getWriter()) {
            if (writer.checkError()) {
                LOGGER.error("Client disconnected");
                throw new IOException("Client disconnected");
            }
            writer.print(JsonSerDesEngine.writeEntity(builder.build()));
        } catch (IOException ex) {
            throw new ServletException(ex);
        }

        return builder.build();
    }
}
