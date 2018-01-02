package org.nashorn.server.async.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.nashorn.server.Command;
import org.nashorn.server.PathVariableSupplier;
import org.nashorn.server.ScriptResponse;
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

        ScriptResponse.Builder builder = new ScriptResponse.Builder();
        if (unit.finishedExceptionally()) {
            builder.exceptionally(unit.getCause());
            builder.setData(unit.getErrorOutput());
            builder.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            builder.isFinished(true);
            for (String name : response.getHeaderNames()) {
                builder.setHeader(name, response.getHeader(name));
            }
        } else {
            builder.setStatus(HttpServletResponse.SC_OK);
            builder.isFinished(unit.isDone());
            for (String name : response.getHeaderNames()) {
                builder.setHeader(name, response.getHeader(name));
            }
            builder.setData(unit.getResultOutput());
            StringBuffer url = request.getRequestURL();
            builder.setHrefSelf(url.append("/").append(id).toString());
        }

        ScriptResponse sr = builder.build();
        ObjectMapper mapper = new ObjectMapper();
        String bresp = mapper.writeValueAsString(sr);

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
