package org.nashorn.server.core;

import javax.script.*;
import java.io.Reader;
import java.io.Writer;

public class ReaderNashornProcessor extends AbstractNashornProcessor {

    private Reader reader;

    private ReaderNashornProcessor(ReaderNashornProcessorBuilder builder) {
        super(builder);
        this.reader = reader;
    }

    @Override
    public Result eval() throws ScriptException {
        Compilable compilable = (Compilable) engine;
        CompiledScript compiledScript = compilable.compile(reader);
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

    public static ReaderNashornProcessorBuilder newBuilder() {
        return new ReaderNashornProcessor.ReaderNashornProcessorBuilder();
    }

    private static class ReaderNashornProcessorBuilder extends AbstractNashornProcessorBuilder {

        private Reader reader;

        public ReaderNashornProcessorBuilder setReader(Reader reader) {
            this.reader = reader;
            return this;
        }

        @Override
        public NashornProcessor build() {
            return new ReaderNashornProcessor(this);
        }
    }
}