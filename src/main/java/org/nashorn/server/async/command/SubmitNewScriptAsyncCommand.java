package org.nashorn.server.async.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nashorn.server.Command;
import org.nashorn.server.ScriptEntity;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.core.ExecutionUnitPool;
import org.nashorn.server.core.NashornScriptCompiler;
import org.nashorn.server.db.InMemoryStorage;

import javax.script.CompiledScript;
import javax.script.ScriptException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SubmitNewScriptAsyncCommand implements Command {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

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

        long id = InMemoryStorage.instance().create(unit);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setHeader("Location", createLocation(id, req));
    }

    private ScriptEntity getScriptEntity(HttpServletRequest req) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(req.getReader(), ScriptEntity.class);
    }

    private String createLocation(long id, HttpServletRequest req) {
        return String.format("%s/%d", req.getRequestURL().toString(), id);
    }
}
