package org.nashorn.server;

import javax.script.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.ExecutorService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nashorn.server.core.NashornCompilableEngineFactory;
import org.nashorn.server.core.NashornExecutionResult;
import org.nashorn.server.core.NashornProcessor;
import org.nashorn.server.core.NashornScriptCompiler;

@WebServlet(name = "blockServlet", urlPatterns = {"/block"}, loadOnStartup = 1)
public class BlockServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/plain;charset=UTF-8");

        ScriptEntity se = getScriptEntity(req);
        String script = se.getScript();

        NashornCompilableEngineFactory ncef = new NashornCompilableEngineFactory();

        ScriptContext context = new SimpleScriptContext();
        context.setWriter(new StringWriter());
        context.setErrorWriter(new StringWriter());

        Compilable compilable = ncef.newCompilableEngine(context);
        NashornScriptCompiler compiler = new NashornScriptCompiler(compilable);

        NashornProcessor processor = null;
        try {
            processor = compiler.compile(script);
        } catch (ScriptException ex) {
            throw new ServletException(ex);
        }

        ExecutorService executor = CommonPool.getInstance();
        NashornExecutionResult<Void> executionResult = null;

        try {
            executionResult = processor.evalAsync(executor);
        } catch (ScriptException ex) {
            throw new ServletException(ex);
        }

        Writer writer = executionResult.getOutputWriter();
        StringWriter sw = (StringWriter) writer;
        StringBuffer buf = sw.getBuffer();

        try (final PrintWriter responseWriter = resp.getWriter()) {
            int prevSize   = 0;
            int futureSize = 0;
            do  {
                if (responseWriter.checkError()) {
                    throw new IOException();
                }
                prevSize = futureSize;
                futureSize = buf.length();
                responseWriter.write(buf.substring(prevSize, futureSize));
            } while (!executionResult.isDone());
        } catch (IOException ex) {
            executionResult.cancel(true);
            throw new ServletException(ex);
        }
    }

    private ScriptEntity getScriptEntity(HttpServletRequest req) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(req.getReader(), ScriptEntity.class);
    }
}
