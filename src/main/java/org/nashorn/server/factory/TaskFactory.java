package org.nashorn.server.factory;

import javax.script.*;
import java.io.StringWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.atomic.AtomicLong;

public class TaskFactory {

    private static final String DEFAULT_ENGINE_NAME = "nashorn";

    private static final ScriptEngineManager MANAGER = new ScriptEngineManager();

    private static AtomicLong count = new AtomicLong();

    public static synchronized RunnableFuture<Object> newScriptTask(String code) throws ScriptException {
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

        Callable<Object> callable = Executors.callable(runnable);

        System.out.println("CALLABLE FROM RUNNABLE " + callable);

        RunnableFuture<Object> st = new ScriptTask<Object>(callable, engine, count.getAndIncrement());
        System.out.println("SCRIPT TASK " + st);
        return st;
    }

    private static String prepareCode(String code) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("function run() {").append(code).append("};");
        return buffer.toString();
    }
}
