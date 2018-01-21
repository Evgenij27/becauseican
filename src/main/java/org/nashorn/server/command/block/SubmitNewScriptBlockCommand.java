package org.nashorn.server.command.block;

import org.apache.log4j.Logger;
import org.nashorn.server.command.AbstractCommand;
import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.core.ExecutionUnitPool;
import org.nashorn.server.util.JsonSerDesEngine;
import org.nashorn.server.util.response.ScriptContent;
import org.nashorn.server.util.response.ScriptResponse;
import org.nashorn.server.util.response.ScriptUnitData;

import javax.script.CompiledScript;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SubmitNewScriptBlockCommand extends AbstractCommand {

    private static final  Logger LOGGER = Logger.getLogger(SubmitNewScriptBlockCommand.class);

    @Override
    public Object execute(HttpServletRequest request, HttpServletResponse response)
            throws CommandExecutionException, ServletException {

        String script = readScriptEntity(getReader(request)).getScript();
        LOGGER.info("SCRIPT : " + script);

        CompiledScript compiledScript = compileScript(script);

        ExecutionUnit unit = ExecutionUnitPool.instance().evalAsync(compiledScript);

        ScriptResponse.Builder builder = new ScriptResponse.Builder();

        builder.copyHeadersFrom(response);

        try (final PrintWriter writer = response.getWriter()) {
            do {
                sleep(1000);
                checkWriter(writer);

                ScriptUnitData sud = new ScriptUnitData(unit);

                ScriptContent sc = new ScriptContent();
                sc.setScript(sud);

                builder.addContent(sc);

                String jsonResp = JsonSerDesEngine.writeEntity(builder.build());
                writer.write(jsonResp);
                
                LOGGER.info("Unit is Done? " + unit.isDone());
            } while ((!unit.isDone()));
            LOGGER.info("WRITING FINISHED");
        } catch (IOException ex) {
            throw new ServletException(ex);
        }

        return null;
    }

    private void sleep(long millis) throws ServletException {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ServletException(e);
        }
    }

    private void checkWriter(PrintWriter writer) throws IOException {
        if (writer.checkError()) throw new IOException("Client disconnected");
    }

}