package org.nashorn.server.core;

import javax.script.ScriptException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class NashornFrontImpl {

    private ExecutorService service;
    private NashornProcessor processor;

    public NashornFrontImpl(SimpleNashornProcessor processor, ExecutorService service) {
        this.processor = processor;
        this.service   = service;
    }

    private Future<NashornProcessor> processorFuture;

    public void evalAndWait(String script) throws ScriptException {

    }

    public void evalAndRelease(String script) throws ScriptException {

    }
}
