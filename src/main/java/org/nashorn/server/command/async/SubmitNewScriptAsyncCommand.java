package org.nashorn.server.command.async;

import org.apache.log4j.Logger;
import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.HttpRequestEntity;
import org.nashorn.server.HttpResponseEntity;
import org.nashorn.server.command.AbstractCommand;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.core.ExecutionUnitPool;
import org.nashorn.server.db.InMemoryStorage;
import org.nashorn.server.util.response.Href;
import org.nashorn.server.util.response.ScriptResponse;

import javax.script.CompiledScript;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

public class SubmitNewScriptAsyncCommand extends AbstractCommand {

    private static final Logger LOGGER = Logger.getLogger(SubmitNewScriptAsyncCommand.class);

    @Override
    public Object execute(HttpRequestEntity request, HttpResponseEntity response)
            throws CommandExecutionException, ServletException {

        String script = readScriptEntity(getReader(request)).getScript();
        CompiledScript compiledScript = compileScript(script);
        ExecutionUnit unit = ExecutionUnitPool.instance().evalAsync(compiledScript);
        long id = InMemoryStorage.instance().create(unit);

        LOGGER.info("BUILD RESPONSE");
        response.setStatus(HttpServletResponse.SC_CREATED);
        Href.Builder hb = new Href.Builder(new StringBuilder(request.getRequestURL()));
        response.setHeader("Location", hb.append(id).build().getSelf());

        ScriptResponse.Builder builder = new ScriptResponse.Builder();
        builder.statusCreated();
        builder.copyHeadersFrom(response);
        builder.noContent();

        return builder.build();
    }
}
