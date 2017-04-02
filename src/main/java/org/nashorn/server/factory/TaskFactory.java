package org.nashorn.server.factory;

import javax.script.*;

public class TaskFactory {

    private TaskFactory() {}

        public static Runnable newTask(String code, ScriptEngine engine) throws ScriptException {
            String newCode = prepareCode(code);

            Compilable compilableEngine = (Compilable) engine;
            CompiledScript compiledScript = compilableEngine.compile(newCode);
            compiledScript.eval();
            Invocable inv = (Invocable) compiledScript.getEngine();
            Runnable runnable = inv.getInterface(Runnable.class);
            System.out.println("RUNNABLE IS " + runnable);
            return runnable;
        }

        private static String prepareCode(String code) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("function run() {").append(code).append("};");
            return buffer.toString();
        }



}
