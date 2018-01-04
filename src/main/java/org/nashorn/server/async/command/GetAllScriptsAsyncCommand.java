package org.nashorn.server.async.command;

import org.apache.log4j.Logger;

import org.nashorn.server.*;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.db.InMemoryStorage;

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
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        Set<ConcurrentMap.Entry<Long, ExecutionUnit>> units = InMemoryStorage.instance().getAllUnits();

        StringBuffer url = request.getRequestURL();

        ScriptResponse.Builder respBuilder    = new ScriptResponse.Builder();
        ScriptContent.Builder  contentBuilder = new ScriptContent.Builder();
        Href.Builder           hrefBuilder    = new Href.Builder(url);


        respBuilder.copyHeadersFrom(response);
        respBuilder.statusOK();

        for (ConcurrentMap.Entry<Long, ExecutionUnit> unit : units) {
            ExecutionUnit u = unit.getValue();

            hrefBuilder.append(unit.getKey());
            contentBuilder.script(u).href(hrefBuilder.build());

            respBuilder.addContent(contentBuilder.build());
        }

        JsonSerDesEngine.indentOutput(true);

        try (final PrintWriter writer = response.getWriter()) {
            if (writer.checkError()) {
                LOGGER.error("Client disconnected");
                throw new IOException("Client disconnected");
            }
            writer.print(JsonSerDesEngine.writeEntity(respBuilder.build()));
        } catch (IOException ex) {
            throw new ServletException(ex);
        }

        JsonSerDesEngine.indentOutput(false);
    }
}
