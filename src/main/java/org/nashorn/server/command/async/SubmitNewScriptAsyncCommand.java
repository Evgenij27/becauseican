package org.nashorn.server.command.async;

import org.apache.log4j.Logger;
import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.HttpRequestEntity;
import org.nashorn.server.HttpResponsePublisher;
import org.nashorn.server.command.AbstractCommand;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.core.ExecutionUnitPool;
import org.nashorn.server.db.InMemoryStorage;
import org.nashorn.server.util.response.UriBuilder;

import javax.script.CompiledScript;
import javax.servlet.ServletException;

public class SubmitNewScriptAsyncCommand extends AbstractCommand {

    private static final Logger LOGGER = Logger.getLogger(SubmitNewScriptAsyncCommand.class);

    @Override
    public void execute(HttpRequestEntity request, HttpResponsePublisher pub)
            throws CommandExecutionException, ServletException {

        String script = readScriptEntity(getReader(request)).getScript();
        CompiledScript compiledScript = compileScript(script);
        ExecutionUnit unit = ExecutionUnitPool.instance().evalAsync(compiledScript);
        long id = InMemoryStorage.instance().create(unit);

        String location = new UriBuilder(request).append(id).build();
        pub.statusCreated()
                .noContent()
                .addHeader("Location", location)
                .publish();

    }
}
