package org.nashorn.server.core;

import javax.script.ScriptException;
import java.io.Reader;
import java.io.StringWriter;

public interface NashornFront {

    void eval(String script) throws ProcessException, ScriptException;

    void eval(Reader reader) throws ProcessException, ScriptException;

    StringWriter getBuffer();

    StringWriter getError();

    boolean isDone() throws ProcessException;

    void cancel() throws ProcessException;

    void cancel(boolean mayInterrupt) throws ProcessException;
}
