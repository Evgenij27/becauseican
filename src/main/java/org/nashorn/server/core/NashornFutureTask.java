package org.nashorn.server.core;

import java.io.StringWriter;
import java.util.concurrent.RunnableFuture;

public interface NashornFutureTask {

    StringWriter getBuffer();

    StringWriter getError();
}
