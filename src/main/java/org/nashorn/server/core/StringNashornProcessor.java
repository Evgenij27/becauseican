package org.nashorn.server.core;

import javax.script.*;
import java.io.Writer;

public class StringNashornProcessor extends AbstractNashornProcessor {

    private final String script;

    private StringNashornProcessor(StringNashornProcessorBuilder builder) {
        super(builder);
        this.script       = builder.script;
    }

    @Override
    public Result process() throws ScriptException {
        Compilable compilable = (Compilable) engine;
        CompiledScript compiledScript = compilable.compile(script);
        compiledScript.eval();
        return new EvalResult();
    }

    private class EvalResult implements Result {

        @Override
        public Writer getResult() {
            return StringNashornProcessor.this.resultWriter;
        }

        @Override
        public Writer getError() {
            return StringNashornProcessor.this.errorWriter;
        }
    }

    public static AbstractNashornProcessorBuilder newBuilder(String script) {
        return new StringNashornProcessorBuilder(script);
    }

    private static class StringNashornProcessorBuilder extends AbstractNashornProcessorBuilder {

        private String script;

        private StringNashornProcessorBuilder(String script) {
            this.script = script;
        }

        @Override
        public NashornProcessor build() {
            return new StringNashornProcessor(this);
        }
    }
}
