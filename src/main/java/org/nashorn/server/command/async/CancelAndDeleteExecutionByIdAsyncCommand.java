package org.nashorn.server.command.async;

import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.HttpRequestEntity;
import org.nashorn.server.HttpResponsePublisher;
import org.nashorn.server.command.AbstractCommand;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.db.InMemoryStorage;
import org.nashorn.server.db.UnitNotFoundException;
import org.nashorn.server.util.PathVariableProcessingException;

import javax.servlet.ServletException;

public class CancelAndDeleteExecutionByIdAsyncCommand extends AbstractCommand {

    @Override
    public void execute(HttpRequestEntity request, HttpResponsePublisher pub)
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

        pub.statusOK();
        pub.content(String.format("Unit with id: %d was deleted.", id));
        pub.publish();
    }
}
