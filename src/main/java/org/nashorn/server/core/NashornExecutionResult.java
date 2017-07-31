package org.nashorn.server.core;

import java.io.Writer;
import java.util.concurrent.Future;

public interface NashornExecutionResult<E> extends Future<E> {

    Writer getOutputWriter();
    Writer getErrorWriter();

}
