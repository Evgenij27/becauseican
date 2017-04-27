package org.nashorn.server.core;

import java.util.concurrent.Callable;

public class NashornWorker implements Callable<Result> {

    private final NashornProcessor processor;

    public NashornWorker(NashornProcessor processor) {
        this.processor = processor;
    }

    @Override
    public Result call() throws Exception {
        System.out.println("CALL................");
        return processor.process();
    }
}
