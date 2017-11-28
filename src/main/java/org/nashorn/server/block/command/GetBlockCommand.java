package org.nashorn.server.block.command;

import org.apache.log4j.Logger;
import org.nashorn.server.Command;
import org.nashorn.server.Snapshot;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.db.InMemoryStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GetBlockCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(GetBlockCommand.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        LOGGER.info("Start GetBlockCommand");
        long id = Long.parseLong((String) req.getAttribute("id"));
        LOGGER.info(String.format("id = %d\n", id));
        ExecutionUnit unit = getExecutionUnitFromStorageById(id);
        LOGGER.info("Unit " + unit);
        try (final PrintWriter writer = resp.getWriter()) {
            do {
                writeToClient(writer, unit.takeSnapshot());
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    LOGGER.error(e);
                }
            } while (!unit.isDone());
            LOGGER.info("Done writing");
        } catch (IOException ex) {
            unit.cancel(true);
            LOGGER.error(ex);
            throw new ServletException(ex);
        }
        LOGGER.info("End GetBlockCommand");
    }

    private ExecutionUnit getExecutionUnitFromStorageById(long id) {
        return InMemoryStorage.instance().read(id);
    }

    private void writeToClient(PrintWriter writer, Snapshot snapshot) throws IOException {
        if (writer.checkError()) {
            throw new IOException();
        }
        LOGGER.info("Snapshot is : " + snapshot);
        writer.write(snapshot.toString());
    }
}
