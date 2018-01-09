package org.nashorn.server.async.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.nashorn.server.Command;
import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.core.ExecutionUnitPool;
import org.nashorn.server.core.NashornScriptCompiler;
import org.nashorn.server.db.InMemoryStorage;
import org.nashorn.server.util.ScriptEntity;
import org.nashorn.server.util.response.Href;
import org.nashorn.server.util.response.HrefBuilder;
import org.nashorn.server.util.response.ScriptResponse;

import javax.script.CompiledScript;
import javax.script.ScriptException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

public class SubmitNewScriptAsyncCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(SubmitNewScriptAsyncCommand.class);

    @Override
    public Object execute(HttpServletRequest request, HttpServletResponse response)
            throws CommandExecutionException, ServletException {

        String script;
        try {
            script = getScriptEntity(request.getReader()).getScript();
        } catch (IOException ex) {
            LOGGER.error(ex);
            throw new CommandExecutionException("Error during parsing JSON.");
        }

        NashornScriptCompiler compiler = new NashornScriptCompiler();

        CompiledScript compiledScript;
        try {
            compiledScript = compiler.compile(script);
        } catch (ScriptException ex) {
            LOGGER.error(ex);
            throw new CommandExecutionException(String.format("Compilation error : %s", ex.getMessage()));
        }

        ExecutionUnit unit = ExecutionUnitPool.instance().evalAsync(compiledScript);

        long id = InMemoryStorage.instance().create(unit);

        response.setStatus(HttpServletResponse.SC_CREATED);
        HrefBuilder hb = Href.newBuilder(new StringBuilder(request.getRequestURL()));
        response.setHeader("Location", hb.append(id).build().getSelf());

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
}
