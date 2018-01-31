package org.nashorn.server.command.async;

import org.apache.log4j.Logger;
import org.nashorn.server.HttpRequestEntity;
import org.nashorn.server.HttpResponseEntity;
import org.nashorn.server.command.AbstractCommand;
import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.db.InMemoryStorage;
import org.nashorn.server.db.UnitNotFoundException;
import org.nashorn.server.util.PathVariableProcessingException;
import org.nashorn.server.util.response.Href;
import org.nashorn.server.util.response.ScriptContent;
import org.nashorn.server.util.response.ScriptResponse;
import org.nashorn.server.util.response.ScriptUnitData;

import javax.servlet.ServletException;

public class GetScriptByIdAsyncCommand extends AbstractCommand {

    private static final Logger LOGGER = Logger.getLogger(GetScriptByIdAsyncCommand.class);

    @Override
    public Object execute(HttpRequestEntity request, HttpResponseEntity response)
            throws CommandExecutionException, ServletException {

        long id;
        try {
            id = request.supplyAsLong("id");
        } catch (PathVariableProcessingException ex) {
            LOGGER.error(ex);
            throw new CommandExecutionException(ex.getMessage());
        }

        ExecutionUnit unit;
        try {
            unit = InMemoryStorage.instance().read(id);
        } catch (UnitNotFoundException ex) {
            LOGGER.error(ex);
            throw new CommandExecutionException(ex.getMessage());
        }

        ScriptResponse.Builder builder = new ScriptResponse.Builder();

        builder.statusOK();
        builder.copyHeadersFrom(response);

        ScriptContent sc = new ScriptContent();
        sc.setScript(new ScriptUnitData(unit));
        sc.setHref(new Href.Builder(new StringBuilder(request.getRequestURL())).build());

        builder.addContent(sc);

        return builder.build();
    }
}
