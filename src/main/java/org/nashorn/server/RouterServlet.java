package org.nashorn.server;

import javax.script.*;
import javax.servlet.*;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nashorn.server.core.NashornCompilableEngineFactory;
import org.nashorn.server.core.NashornExecutionTask;
import org.nashorn.server.core.NashornScriptCompiler;

import org.apache.log4j.Logger;

@WebServlet(
        name = "routerServlet",
        urlPatterns = {"/*"},
        loadOnStartup = 1
       )
public class RouterServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(RouterServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        LOGGER.info("START SERVICE");

        String apiVersion = (String) req.getAttribute("API_VERSION");
        String apiType    = (String) req.getAttribute("API_TYPE");

        LOGGER.debug("API VERSION: " + apiVersion);
        LOGGER.debug("API TYPE   : " + apiType);







        /*

        ScriptEntity se = getScriptEntity(req);
        String script = se.getScript();

        NashornCompilableEngineFactory ncef = new NashornCompilableEngineFactory();

        ScriptContext context = new SimpleScriptContext();
        context.setWriter(new StringWriter());
        context.setErrorWriter(new StringWriter());

        Compilable compilable = ncef.newCompilableEngine(context);
        NashornScriptCompiler compiler = new NashornScriptCompiler(compilable);

        NashornExecutionTask task = null;
        try {
            task = compiler.compile(script);
        } catch (ScriptException ex) {
            throw new ServletException(ex);
        }

        ExecutorService executor = CommonPool.getInstance();
        executor.submit(task);

        StringBuffer buf = task.getOutputBuffer();

        try (final PrintWriter responseWriter = resp.getWriter()) {
            try {
                int prevSize = 0;
                int futureSize = 0;
                do {
                    if (responseWriter.checkError()) {
                        throw new IOException();
                    }
                    prevSize = futureSize;
                    futureSize = buf.length();
                    responseWriter.write(buf.substring(prevSize, futureSize));
                } while (!task.isDone());
        */
                /*
                    Check for errors during execution
                 */
        /*
                task.get();
            } catch (InterruptedException interrupt) {
                interrupt.printStackTrace();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
            }

        } catch (IOException ex) {
            task.cancel(true);
            throw new ServletException(ex);
        }
        */
}

    
    private ScriptEntity getScriptEntity(HttpServletRequest req) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(req.getReader(), ScriptEntity.class);
    }


}
