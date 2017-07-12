package org.nashorn.server.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nashorn.server.ScriptEntity;
import org.nashorn.server.annotation.PostMapping;
import org.nashorn.server.core.*;
import org.nashorn.server.db.DAO;
import org.nashorn.server.db.Memory;

import javax.script.ScriptException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@PostMapping(path = "/nashorn")
public class SubmitScriptCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

       /* ObjectMapper mapper = new ObjectMapper();
        ScriptEntity scriptEntity = mapper.readValue(request.getReader(), ScriptEntity.class);


        ExecutorService executorService = Executors.newFixedThreadPool(10);

        AbstractNashornProcessorBuilder builder =
                StringNashornProcessor.newBuilder(scriptEntity.getScript()).
                saveResultTo(new StringWriter()).
                saveErrorTo(new StringWriter());

        NashornProcessor processor = null;

        try {
            processor = builder.build();
        } catch (ScriptException ex) {
            throw new ServletException(ex);
        }

        NashornWorker worker = new NashornWorker(processor);

        Future<Result> result = executorService.submit(worker);

        DAO memory = Memory.getInstance();
        long id = memory.create(result);

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setHeader("Location", String.valueOf(id));*/
    }
}
