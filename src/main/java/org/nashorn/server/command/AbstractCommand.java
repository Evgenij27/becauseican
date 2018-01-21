package org.nashorn.server.command;

import org.apache.log4j.Logger;
import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.core.NashornScriptCompiler;
import org.nashorn.server.util.JsonSerDesEngine;
import org.nashorn.server.util.ScriptEntity;

import javax.script.CompiledScript;
import javax.script.ScriptException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public abstract class AbstractCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(AbstractCommand.class);

    protected CompiledScript compileScript(String script) throws CommandExecutionException {
        try {
            return new NashornScriptCompiler().compile(script);
        } catch (ScriptException ex) {
            LOGGER.error(ex);
            throw new CommandExecutionException(String.format("Compilation error : %s", ex.getMessage()));
        }
    }

    protected ScriptEntity readScriptEntity(Reader reader) throws CommandExecutionException {
        try {
            return  JsonSerDesEngine.readEntity(reader);
        } catch (IOException ex) {
            LOGGER.error(ex);
            throw new CommandExecutionException("Error during parsing JSON.");
        }
    }

    protected BufferedReader getReader(HttpServletRequest req) throws ServletException {
        try {
            return req.getReader();
        } catch (IOException ex) {
            LOGGER.error(ex);
            throw new ServletException(ex);
        }
    }
}
