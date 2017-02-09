package org.nashorn.server.factory;

import javax.script.*;
import java.io.StringWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.atomic.AtomicLong;

public class TaskFactory {

    private static TaskFactory instance;

    public static synchronized TaskFactory getInstance() {
        if (instance == null) {
            instance = new TaskFactory();
        }
        return instance;
    }

    private TaskFactory() {}

    private static final String DEFAULT_ENGINE_NAME = "nashorn";

    private static final ScriptEngineManager MANAGER = new ScriptEngineManager();

    private static final AtomicLong id = new AtomicLong();

    public synchronized ScriptTask newScriptTask(String code) throws ScriptException {
        String newCode = prepareCode(code);

        ScriptEngine engine = MANAGER.getEngineByName(DEFAULT_ENGINE_NAME);
        engine.getContext().setWriter(new StringWriter());
        engine.getContext().setErrorWriter(new StringWriter());

        Compilable compilableEngine = (Compilable) engine;
        CompiledScript compiledScript = compilableEngine.compile(newCode);

        compiledScript.eval();

        Invocable inv = (Invocable) compiledScript.getEngine();

        Runnable runnable = inv.getInterface(Runnable.class);

        System.out.println("RUNNABLE IS " + runnable);

        FutureTask<?> futureTask = new FutureTask<Void>(runnable, null);
        ScriptTask st = new ScriptTask(futureTask, engine, id.getAndIncrement());
        System.out.println("SCRIPT TASK " + st);
        return st;
    }

    private String prepareCode(String code) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("function run() {").append(code).append("};");
        return buffer.toString();
    }
}
