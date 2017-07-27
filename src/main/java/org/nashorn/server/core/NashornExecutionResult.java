package org.nashorn.server.core;

import java.util.concurrent.Future;

public interface NashornExecutionResult<E> extends Future<E> {

    StringBuffer getOutputBuffer();
    StringBuffer getErrorBuffer();

}
