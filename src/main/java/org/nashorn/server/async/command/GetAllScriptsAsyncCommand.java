package org.nashorn.server.async.command;

import org.apache.log4j.Logger;

import org.nashorn.server.Command;
import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.db.InMemoryStorage;
import org.nashorn.server.util.JsonSerDesEngine;
import org.nashorn.server.util.response.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public class GetAllScriptsAsyncCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(GetAllScriptsAsyncCommand.class);

    @Override
    public Object execute(HttpServletRequest request, HttpServletResponse response)
            throws CommandExecutionException, ServletException {

        Set<ConcurrentMap.Entry<Long, ExecutionUnit>> units = InMemoryStorage.instance().getAllUnits();

        ScriptResponse.Builder respBuilder = new ScriptResponse.Builder();
        HrefBuilder hrefBuilder
                = Href.newBuilder(new StringBuilder(request.getRequestURL()));

        respBuilder.copyHeadersFrom(response);
        respBuilder.statusOK();

        for (ConcurrentMap.Entry<Long, ExecutionUnit> unit : units) {
            ExecutionUnit u = unit.getValue();

            hrefBuilder.append(unit.getKey());
            ScriptUnitData sud = new ScriptUnitData(u);
            ScriptContent content = new ScriptContent();
            content.setScript(sud);
            content.setHref(hrefBuilder.build());

            respBuilder.addContent(content);
        }


        try (final PrintWriter writer = response.getWriter()) {
            if (writer.checkError()) {
                LOGGER.error("Client disconnected");
                throw new IOException("Client disconnected");
            }
            writer.print(JsonSerDesEngine.writeEntity(respBuilder.build()));
        } catch (IOException ex) {
            throw new ServletException(ex);
        }

        return respBuilder.build();
    }
}
