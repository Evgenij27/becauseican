package org.nashorn.server.command;

import org.nashorn.server.Bucket;
import org.nashorn.server.Handler;
import org.nashorn.server.annotation.PostMapping;
import org.nashorn.server.db.DAO;
import org.nashorn.server.db.Memory;
import org.nashorn.server.factory.TaskFactory;
import org.nashorn.server.factory.TaskManager;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.concurrent.Future;


@PostMapping(path = "/nashorn/")
public class AnotherCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("Hello POST!!!");

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.getContext().setWriter(new StringWriter());
        engine.getContext().setErrorWriter(new StringWriter());

        TaskManager taskManager = TaskManager.getInstance();
        taskManager.setEngine(engine);

        TaskFactory taskFactory = taskManager.newTaskFactory();
        Runnable task = null;

        try {
            task = taskFactory.newTask("print('Hello World! JavaScript on Java')");
        } catch (ScriptException ex) {
            throw new ServletException();
        }

        Handler handler = Handler.INSTANCE;
        Future<?> future = handler.handle(task);

        Bucket bucket = new Bucket.Builder()
                .setEngine(engine)
                .setRunnable(task)
                .setFuture(future)
                .build();

        DAO memory = Memory.INSTANCE;
        long id = memory.create(bucket);

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setHeader("Location", String.valueOf(id));

    }
}
