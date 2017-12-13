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

public class SubmitNewScriptCommand implements Command {

    private final static Logger logger = Logger.getLogger(SubmitNewScriptCommand.class);

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        logger.info("Greeting command");

        String script = null;
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

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        try (final PrintWriter writer = resp.getWriter()) {

            do {
                sleep(1000);
                checkWriter(writer);
                if (unit.finishedExceptionally()) {
                    logger.info("SEND ERROR RESPONSE");
                    writer.write(sendErrorResponse(unit));
                } else {
                    logger.info("SEND SUCCESS RESPONSE");
                    writer.write(sendSuccessResponse(unit));
                }
                logger.info("Unit is Done? " + unit.isDone());
            } while ((!unit.isDone()));

            logger.info("WRITTING FINISHED");

        } catch (IOException ex) {
            throw new ServletException(ex);
        }
    }

    private void sleep(long millis) throws ServletException {
        try {
            Thread.sleep(1000);
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

    private String sendSuccessResponse(ExecutionUnit unit) throws IOException {
        String result = unit.getResultOutput();
        logger.info(result);
        ScriptResponse resp = new ScriptResponse(HttpServletResponse.SC_OK,
                StatusNames.SUCCESS.name().toLowerCase(), result);
        return convertToJson(resp);
    }

    private String sendErrorResponse(ExecutionUnit unit) throws IOException {
        String error = unit.getErrorOutput();
        logger.info(error);
        String[] splittedString = error.split(":");

        String errorCause = splittedString[0].trim();
        String errorMessage = splittedString[1].trim();

        ScriptResponse resp = new ErrorScriptResponse(HttpServletResponse.SC_BAD_REQUEST,
                StatusNames.ERROR.name().toLowerCase(), errorCause, errorMessage);
        return convertToJson(resp);
    }

    private String convertToJson(Object value) throws IOException {
            return mapper.writeValueAsString(value);
    }
}
