package org.nashorn.server.block.command;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.script.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.nashorn.server.Command;
import org.nashorn.server.ScriptEntity;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.core.ExecutionUnitPool;
import org.nashorn.server.core.NashornScriptCompiler;
import org.nashorn.server.db.InMemoryStorage;

public class GreetingBlockCommand implements Command {

    private final Logger logger = Logger.getLogger(GreetingBlockCommand.class);

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

        ExecutionUnit unit = ExecutionUnitPool.instance().evalAsync(compiledScript);

        long id = InMemoryStorage.instance().create(unit);

        logger.info("Unit in Command : " + unit);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        String location = String.format("%s/%d", formatRequestURL(req), id);
        resp.setHeader("Location", location);
        logger.info("End Command");
    }

    private ScriptEntity getScriptEntity(HttpServletRequest req) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(req.getReader(), ScriptEntity.class);
    }

    private String formatRequestURL(HttpServletRequest req) {
        String url = req.getRequestURL().toString();
        if (url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        }
        return url;
    }
}
