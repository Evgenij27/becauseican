package org.nashorn.server.core;

import javax.script.*;
import java.io.Reader;
import java.io.Writer;

public class ReaderNashornProcessor extends AbstractNashornProcessor {

    private final CompiledScript compiledScript;

    private ReaderNashornProcessor(ReaderNashornProcessorBuilder builder) throws ScriptException {
        super(builder);
        this.compiledScript = compilable.compile(builder.reader);
    }

    @Override
    public Result process() throws ScriptException {
        compiledScript.eval();
        return new ReaderResult();
    }

    private class ReaderResult implements Result {

        @Override
        public Writer getResult() {
            return ReaderNashornProcessor.this.resultWriter;
        }

        @Override
        public Writer getError() {
            return ReaderNashornProcessor.this.errorWriter;
        }
    }

    public static AbstractNashornProcessorBuilder newBuilder(Reader reader) {
        return new ReaderNashornProcessorBuilder(reader);
    }

    private static class ReaderNashornProcessorBuilder extends AbstractNashornProcessorBuilder {

        private Reader reader;

        private ReaderNashornProcessorBuilder(Reader reader) {
            this.reader = reader;
        }

        @Override
        public NashornProcessor build() throws ScriptException {
            return new ReaderNashornProcessor(this);
        }
    }
}