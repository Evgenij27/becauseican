package org.nashorn.server.core;

import javax.script.ScriptException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class NashornFactory {

    private ExecutorService service;

    private class NashornFrontBlocking implements NashornFront {

        private NashornProcessor processor;
        private Future<NashornProcessor> future;

        private NashornFrontBlocking(NashornProcessor processor) {
            this.processor = processor;
        }

        @Override
        public void eval(String script) throws ProcessException, ScriptException {
            processor.setAndCheck(script);
            future = service.submit(new NashornWorker(processor));
            future.get();
        }

        @Override
        public void eval(Reader reader) throws ProcessException, ScriptException {
            processor.setAndCheck(reader);
            processor.process();
        }

        @Override
        public StringWriter getBuffer() {
            return processor.getBuffer();
        }

        @Override
        public StringWriter getError() {
            return processor.getError();
        }

        @Override
        public boolean isDone() throws ProcessException {
            return false;
        }

        @Override
        public void cancel() throws ProcessException {

        }

        @Override
        public void cancel(boolean mayInterrupt) throws ProcessException {

        }
    }
}
