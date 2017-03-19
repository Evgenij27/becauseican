package org.nashorn.server.factory;

import com.sun.javafx.tk.Toolkit;

import javax.script.*;
import java.io.StringWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.atomic.AtomicLong;

public class TaskManager {

    private static TaskManager instance;

    public static synchronized TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    private TaskManager() {}

    private ScriptEngine engine;

    public void setEngine(ScriptEngine engine) {
        this.engine = engine;
    }

    public TaskFactory newTaskFactory() {
        return new TaskFactoryImpl();
    }

    private class TaskFactoryImpl implements TaskFactory {

        @Override
        public Runnable newTask(String code) throws ScriptException {
            String newCode = prepareCode(code);

            Compilable compilableEngine = (Compilable) engine;
            CompiledScript compiledScript = compilableEngine.compile(newCode);
            compiledScript.eval();
            Invocable inv = (Invocable) compiledScript.getEngine();
            Runnable runnable = inv.getInterface(Runnable.class);
            System.out.println("RUNNABLE IS " + runnable);
            return runnable;
        }

        private String prepareCode(String code) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("function run() {").append(code).append("};");
            return buffer.toString();
        }
    }


}
