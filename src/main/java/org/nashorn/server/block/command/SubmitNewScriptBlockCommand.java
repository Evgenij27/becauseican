package org.nashorn.server.block.command;

import javax.script.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import org.apache.log4j.Logger;
import org.nashorn.server.*;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.core.ExecutionUnitPool;
import org.nashorn.server.core.NashornScriptCompiler;

public class SubmitNewScriptBlockCommand implements Command {

    private static final  Logger logger = Logger.getLogger(SubmitNewScriptBlockCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String script = getScriptFromRequest(request.getReader());

        CompiledScript compiledScript = compiledScript(script);

        ExecutionUnit unit = ExecutionUnitPool.instance().evalAsync(compiledScript);

        ScriptResponse.Builder builder = new ScriptResponse.Builder();

        builder.copyHeadersFrom(response);

        try (final PrintWriter writer = response.getWriter()) {
            do {
                sleep(1000);
                checkWriter(writer);

                ScriptContent sc = new ScriptContent.Builder()
                                .href(new Href.Builder(request.getRequestURL()).build())
                                .script(unit)
                                .build();

                builder.addContent(sc);

                String jsonResp = JsonSerDesEngine.writeEntity(builder.build());
                writer.write(jsonResp);
                
                logger.info("Unit is Done? " + unit.isDone());
            } while ((!unit.isDone()));
            logger.info("WRITTING FINISHED");
        } catch (IOException ex) {
            throw new ServletException(ex);
        }
    }

    private String getScriptFromRequest(Reader reader) throws ServletException {
        String script;
        try {
            script = JsonSerDesEngine.readEntity(reader).getScript();
        } catch (IOException ex) {
            throw new ServletException(ex);
        }
        return script;
    }

    private CompiledScript compiledScript(String script) throws ServletException {
        NashornScriptCompiler compiler = new NashornScriptCompiler();
        CompiledScript compiledScript;
        try {
            compiledScript = compiler.compile(script);
        } catch (ScriptException ex) {
            throw new ServletException(ex);
        }
        return compiledScript;
    }

    private void sleep(long millis) throws ServletException {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ServletException(e);
        }
    }

    private void checkWriter(PrintWriter writer) throws IOException {
        if (writer.checkError()) throw new IOException("Client disconnected");
    }

}