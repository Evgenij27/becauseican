package org.nashorn.server.async.command;

import org.nashorn.server.Command;
import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.util.PathVariableProcessingException;
import org.nashorn.server.db.UnitNotFoundException;
import org.nashorn.server.util.PathVariableSupplier;
import org.nashorn.server.util.response.ScriptResponse;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.db.InMemoryStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CancelAndDeleteExecutionByIdAsyncCommand implements Command {
    @Override
    public Object execute(HttpServletRequest request, HttpServletResponse response)
            throws CommandExecutionException, ServletException {

        PathVariableSupplier pvs = new PathVariableSupplier(request);
        long id = 0;
        try {
            id = pvs.supplyAsLong("id");
        } catch (PathVariableProcessingException ex) {
            throw new CommandExecutionException(ex.getMessage());
        }

        ExecutionUnit unit = null;
        try {
            unit = InMemoryStorage.instance().read(id);
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
