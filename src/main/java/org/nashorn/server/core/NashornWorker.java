package org.nashorn.server.core;

import java.util.concurrent.Callable;

public class NashornWorker implements Callable<NashornProcessor> {

    private final NashornProcessor processor;

    public NashornWorker(NashornProcessor processor) {
        this.processor = processor;
    }

    @Override
    public NashornProcessor call() throws Exception {
        processor.process();
        return processor;
    }
}
