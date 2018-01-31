package org.nashorn.server.command.async;

import org.apache.log4j.Logger;
import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.HttpRequestEntity;
import org.nashorn.server.HttpResponseEntity;
import org.nashorn.server.command.AbstractCommand;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.db.InMemoryStorage;
import org.nashorn.server.util.response.Href;
import org.nashorn.server.util.response.ScriptContent;
import org.nashorn.server.util.response.ScriptResponse;
import org.nashorn.server.util.response.ScriptUnitData;

import javax.servlet.ServletException;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public class GetAllScriptsAsyncCommand extends AbstractCommand {

    private static final Logger LOGGER = Logger.getLogger(GetAllScriptsAsyncCommand.class);

    @Override
    public Object execute(HttpRequestEntity request, HttpResponseEntity response)
            throws CommandExecutionException, ServletException {

        Set<ConcurrentMap.Entry<Long, ExecutionUnit>> units = InMemoryStorage.instance().getAllUnits();

        ScriptResponse.Builder respBuilder = new ScriptResponse.Builder();
        Href.Builder hrefBuilder
                = new Href.Builder(new StringBuilder(request.getRequestURL()));

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
        return respBuilder.build();
    }
}
