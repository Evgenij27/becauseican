package org.nashorn.server.block.command;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.script.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.log4j.Logger;
import org.nashorn.server.*;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.core.ExecutionUnitPool;
import org.nashorn.server.core.NashornScriptCompiler;

public class SubmitNewScriptBlockCommand implements Command {

    private final static Logger logger = Logger.getLogger(SubmitNewScriptBlockCommand.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        String script;
        try {
            script = getScriptEntity(req).getScript();
        } catch (IOException ex) {
           throw new ServletException(ex);
        }

        NashornScriptCompiler compiler = new NashornScriptCompiler();

        CompiledScript compiledScript;
        try {
            compiledScript = compiler.compile(script);
        } catch (ScriptException ex) {
            throw new ServletException(ex);
        }

        ExecutionUnit unit = ExecutionUnitPool.instance().evalAsync(compiledScript);

        try (final PrintWriter writer = resp.getWriter()) {

            do {
                sleep(1000);
                checkWriter(writer);
                String jsonResponse = ScriptResponseFactory.newJsonResponseFor(unit);
                writer.write(jsonResponse);
                logger.info("Unit is Done? " + unit.isDone());
            } while ((!unit.isDone()));

            logger.info("WRITTING FINISHED");

        } catch (IOException ex) {
            throw new ServletException(ex);
        }
    }

    private void sleep(long millis) throws ServletException {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new ServletException(e);
        }
    }

    private void checkWriter(PrintWriter writer) throws IOException {
        if (writer.checkError()) throw new IOException("Client disconnected");
    }

    private ScriptEntity getScriptEntity(HttpServletRequest req) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(req.getReader(), ScriptEntity.class);
    }
}