package org.nashorn.server.command.async;

import org.apache.log4j.Logger;
import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.HttpRequestEntity;
import org.nashorn.server.HttpResponsePublisher;
import org.nashorn.server.command.AbstractCommand;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.db.InMemoryStorage;
import org.nashorn.server.db.UnitNotFoundException;
import org.nashorn.server.util.PathVariableProcessingException;
import org.nashorn.server.util.response.ScriptExecutionUnitData;
import org.nashorn.server.util.response.UriBuilder;

import javax.servlet.ServletException;

public class GetScriptByIdAsyncCommand extends AbstractCommand {

    private static final Logger LOGGER = Logger.getLogger(GetScriptByIdAsyncCommand.class);

    @Override
    public void execute(HttpRequestEntity request, HttpResponsePublisher pub)
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

        String location = new UriBuilder(request).append(id).build();

        ScriptExecutionUnitData unitData = new ScriptExecutionUnitData();
        unitData.setId(id);
        unitData.setUnit(unit);
        unitData.setLocation(location);

        pub.statusOK().content(unitData).publish();

    }
}
