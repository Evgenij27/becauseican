package org.nashorn.server.command.block;

import org.apache.log4j.Logger;
import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.HttpRequestEntity;
import org.nashorn.server.HttpResponsePublisher;
import org.nashorn.server.command.AbstractCommand;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.core.ExecutionUnitPool;

import javax.script.CompiledScript;
import javax.servlet.ServletException;

public class SubmitNewScriptBlockCommand extends AbstractCommand {

    private static final  Logger LOGGER = Logger.getLogger(SubmitNewScriptBlockCommand.class);

    @Override
    public void execute(HttpRequestEntity request, HttpResponsePublisher pub)
            throws CommandExecutionException, ServletException {

        String script = readScriptEntity(getReader(request)).getScript();
        LOGGER.info("SCRIPT : " + script);

        CompiledScript compiledScript = compileScript(script);

        ExecutionUnit unit = ExecutionUnitPool.instance().evalAsync(compiledScript);

        pub.statusOK();

        do {
            sleep(1000);

            pub.content(unit);
            pub.publish();

            LOGGER.info("Unit is Done? " + unit.isDone());
        } while ((!unit.isDone()));
        LOGGER.info("WRITING FINISHED");

    }

    private void sleep(long millis) throws ServletException {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ServletException(e);
        }
    }
}