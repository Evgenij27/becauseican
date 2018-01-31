package org.nashorn.server.command.async;

import org.nashorn.server.HttpRequestEntity;
import org.nashorn.server.HttpResponseEntity;
import org.nashorn.server.command.AbstractCommand;
import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.util.PathVariableProcessingException;
import org.nashorn.server.db.UnitNotFoundException;
import org.nashorn.server.util.response.ScriptResponse;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.db.InMemoryStorage;

import javax.servlet.ServletException;

public class CancelAndDeleteExecutionByIdAsyncCommand extends AbstractCommand {
    @Override
    public Object execute(HttpRequestEntity request, HttpResponseEntity response)
            throws CommandExecutionException, ServletException {

        long id;
        try {
            id = request.supplyAsLong("id");
        } catch (PathVariableProcessingException ex) {
            throw new CommandExecutionException(ex.getMessage());
        }

        ExecutionUnit unit;
        try {
            unit = InMemoryStorage.instance().read(id);
            InMemoryStorage.instance().delete(id);
        } catch (UnitNotFoundException ex) {
            throw new CommandExecutionException(ex.getMessage());
        }

        unit.cancel(true);
        
        ScriptResponse.Builder respBuilder = new ScriptResponse.Builder();
        respBuilder.statusOK();
        respBuilder.copyHeadersFrom(response);
        respBuilder.noContent();
        respBuilder.withMessage("done!");

        return respBuilder.build();
    }
}
