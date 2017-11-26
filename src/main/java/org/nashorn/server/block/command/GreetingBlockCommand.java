package org.nashorn.server.block.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nashorn.server.AbstractCommand;

import javax.script.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.log4j.Logger;
import org.nashorn.server.ScriptEntity;
import org.nashorn.server.Snapshot;
import org.nashorn.server.block.ScheduledExecutionUnitPool;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.core.NashornScriptCompiler;

public class GreetingBlockCommand extends AbstractCommand {

    private final Logger logger = Logger.getLogger(GreetingBlockCommand.class);

    public GreetingBlockCommand(ScheduledExecutionUnitPool scheduledPool) {
        super(scheduledPool);
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        logger.info("Greeting command");

        String script = getScriptEntity(req).getScript();

        NashornScriptCompiler compiler = new NashornScriptCompiler();

        CompiledScript compiledScript;
        try {
            compiledScript = compiler.compile(script);
        } catch (ScriptException ex) {
            throw new ServletException(ex);
        }

        ExecutionUnit schedUnit = scheduledPool.getUnit();
        logger.info("SchedUnit in Command : " + schedUnit);
        try {
            schedUnit.evalAsync(compiledScript);
            try (final PrintWriter writer = resp.getWriter()) {
                do {
                    Snapshot snapshot = schedUnit.takeSnapshot();
                    writeResponse(writer, snapshot);
                    logger.info("Write to Client : " + snapshot);
                } while (!schedUnit.isDone());
            } catch (IOException ex) {
                schedUnit.cancel(true);
                logger.error("IOException.... execution canceled...\n", ex);
            }
        } finally {
            logger.info("Return Unit to the Pool");
            scheduledPool.returnUnit(schedUnit);
        }
        logger.info("End Command");
    }

    private String getRequestBody(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (final BufferedReader br = req.getReader()) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException ex) {
            logger.error("Error during reading request body", ex);
            throw new IOException();
        }
        return sb.toString();
    }

    private ScriptEntity getScriptEntity(HttpServletRequest req) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(req.getReader(), ScriptEntity.class);
    }

    private void writeResponse(PrintWriter writer, Snapshot snapshot) throws ServletException {
        try {
            if (writer.checkError()) {
                throw new IOException();
            }
            writer.print(snapshot.toString());
        } catch (IOException ex) {
            throw new ServletException(ex);
        }
    }
}
