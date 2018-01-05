package org.nashorn.server.async.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.nashorn.server.Command;
import org.nashorn.server.util.JsonSerDesEngine;
import org.nashorn.server.util.ScriptEntity;
import org.nashorn.server.util.response.ScriptResponse;
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
import java.io.PrintWriter;
import java.io.Reader;

public class SubmitNewScriptAsyncCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(SubmitNewScriptAsyncCommand.class);

    @Override
    public Object execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String script;
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

        ScriptResponse.Builder builder = new ScriptResponse.Builder();
        builder.statusCreated();
        builder.copyHeadersFrom(response);
        builder.noContent();

        return builder.build();
    }

    private ScriptEntity getScriptEntity(Reader reader) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(reader, ScriptEntity.class);
    }

    private String createLocation(long id, StringBuffer url) {
        return url.append("/").append(id).toString();
    }
}
