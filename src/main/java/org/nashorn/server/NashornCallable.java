package org.nashorn.server;

import javax.script.*;
import java.util.List;
import java.util.concurrent.Callable;

public class NashornCallable implements Callable<ScriptContext> {

    private final ScriptContext context;
    private final ScriptEntity scriptEntity;

    public NashornCallable(ScriptContext context, ScriptEntity scriptEntity) {
        this.context = context;
        this.scriptEntity = scriptEntity;
    }

    private ScriptEngine initEngine() {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.setContext(this.context);
        return engine;
    }

    @Override
    public ScriptContext call() throws Exception {
        ScriptEngine engine = initEngine();

        List<String> functionArguments = scriptEntity.getArguments();

        StringBuilder sb = new StringBuilder();
        functionArguments.stream()
                .map((s) -> s = s + ", ")
                .forEach(sb::append);

        System.out.println("Streamed arguments list result " + sb.toString());

        Compilable compiliableEngine = (Compilable) engine;
        CompiledScript compiledScript = compiliableEngine.compile(scriptEntity.getScript());
        compiledScript.eval();

        Invocable invocable = (Invocable) compiledScript.getEngine();
        invocable.invokeFunction(scriptEntity.getMainFunctionName(), sb.toString());

        return context;
    }
}
