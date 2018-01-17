package org.nashorn.server.async.command;

import org.apache.log4j.Logger;
import org.nashorn.server.Command;
import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.db.InMemoryStorage;
import org.nashorn.server.db.UnitNotFoundException;
import org.nashorn.server.util.PathVariableProcessingException;
import org.nashorn.server.util.response.Href;
import org.nashorn.server.util.PathVariableSupplier;
import org.nashorn.server.util.response.ScriptContent;
import org.nashorn.server.util.response.ScriptResponse;
import org.nashorn.server.util.response.ScriptUnitData;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetScriptByIdAsyncCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(GetScriptByIdAsyncCommand.class);

    @Override
    public Object execute(HttpServletRequest request, HttpServletResponse response)
            throws CommandExecutionException, ServletException {

        PathVariableSupplier pvs = new PathVariableSupplier(request);

        long id = 0;
        try {
            id = pvs.supplyAsLong("id");
        } catch (PathVariableProcessingException ex) {
            LOGGER.error(ex);
            throw new CommandExecutionException(ex.getMessage());
        }

        ExecutionUnit unit = null;
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
