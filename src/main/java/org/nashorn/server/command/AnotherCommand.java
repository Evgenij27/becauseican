package org.nashorn.server.command;


import org.nashorn.server.*;
import org.nashorn.server.annotation.PostMapping;

import javax.script.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nashorn.server.data.Body;
import org.nashorn.server.db.DAO;
import org.nashorn.server.db.Memory;
import org.nashorn.server.factory.TaskFactory;


@PostMapping(path = "/nashorn/")
public class AnotherCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        ScriptEntity scriptEntity = mapper.readValue(request.getReader(), ScriptEntity.class);

        ScriptContext scriptContext = new SimpleScriptContext();
        scriptContext.setWriter(new StringWriter());
        scriptContext.setErrorWriter(new StringWriter());

        NashornCallable nashornCallable = new NashornCallable(scriptContext, scriptEntity);

        NashornWorker worker = new NashornWorker(nashornCallable, scriptContext);

        NashornTaskExecutor.getService().submit(worker);

        DAO memory = Memory.getInstance();

        long taskId = memory.create(worker);

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setHeader("Location", String.valueOf(taskId));
    }
}
