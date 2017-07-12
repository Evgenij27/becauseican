package org.nashorn.server.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nashorn.server.ScriptEntity;

import javax.script.ScriptException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class NashornHandler {

    public void handle(HttpServletRequest req, HttpServletResponse resp, NashornServiceFactory factory)
            throws IOException, ServletException {

        ObjectMapper mapper = new ObjectMapper();
        ScriptEntity entity = mapper.readValue(req.getReader(), ScriptEntity.class);

        NashornService service = factory.newService();

        String script = entity.getScript();

        StringWriter output = new StringWriter();
        StringWriter error  = new StringWriter();

        service.setOutputBuffer(output);
        service.setErrorBuffer(error);

        StringBuffer outputBuf = output.getBuffer();

        int prevSize = 0;
        int newSize  = 0;

        try(final PrintWriter writer = resp.getWriter()) {
            System.out.println("Start service");
            service.submit(script);
            do {
                if (writer.checkError()) {
                    service.cancel(true);
                    throw new IOException("IO Error");
                }

                /*
                    Writting a chunk of data to client
                 */
                newSize = outputBuf.length();
                if ((newSize - prevSize) <= 20) {
                    writer.write(outputBuf.substring(prevSize, newSize));
                    prevSize = newSize;
                } else {
                    writer.write(outputBuf.substring(prevSize, newSize));
                    prevSize = newSize;
                }
            } while (!service.isDone());

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (ScriptException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("isDone?      : " + service.isDone());
        System.out.println("isCancelled? : " + service.isCancelled());
    }
}
