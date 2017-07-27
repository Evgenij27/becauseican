package org.nashorn.server.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nashorn.server.ScriptEntity;

import javax.script.CompiledScript;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class NashornHandler {

    public void handle(HttpServletRequest req, HttpServletResponse resp, NashornServiceFactory factory)
            throws IOException, ServletException {

        resp.setContentType("text/plain;cahrset='UTF-8'");

        ObjectMapper mapper = new ObjectMapper();
        ScriptEntity entity = mapper.readValue(req.getReader(), ScriptEntity.class);

        NashornService service = factory.newService();

        String script = entity.getScript();

        StringWriter output = new StringWriter();
        StringWriter error  = new StringWriter();

        CompiledScript compiledScript = null;
        try {
            compiledScript = NashornScriptCompiler.compile(script);
        } catch (ScriptException ex) {
            ex.printStackTrace();
            throw new ServletException(ex);
        }

        service.setOutputBuffer(output);
        service.setErrorBuffer(error);

        StringBuffer outputBuf = output.getBuffer();
        StringBuffer errorBuf  = error.getBuffer();

        int prevSize, newSize = 0;

        try(final PrintWriter writer = resp.getWriter()) {
            System.out.println("Start service");
            try {
                service.submit(script);
                do {
                    if (writer.checkError()) {
                        service.cancel(true);
                        throw new IOException("IO Error");
                    }
                    // Write to client
                    prevSize = newSize;
                    newSize = outputBuf.length();
                    writer.write(outputBuf.substring(prevSize, newSize));
                } while (!service.isDone());
            } catch (ScriptException ex) {
                ex.printStackTrace();
                writer.write(errorBuf.toString());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println("isDone?      : " + service.isDone());
        System.out.println("isCancelled? : " + service.isCancelled());
    }
}
