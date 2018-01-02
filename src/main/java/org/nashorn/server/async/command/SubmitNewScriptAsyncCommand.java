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
import java.io.Reader;

public class SubmitNewScriptAsyncCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String script = null;
        try {
            script = getScriptEntity(request.getReader()).getScript();
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

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setHeader("Location", createLocation(id, request.getRequestURL()));
    }

    private ScriptEntity getScriptEntity(Reader reader) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(reader, ScriptEntity.class);
    }

    private String createLocation(long id, StringBuffer url) {
        return url.append("/").append(id).toString();
    }
}
