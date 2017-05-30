package org.nashorn.server.core;

import javax.script.ScriptException;
import java.io.Reader;
import java.io.StringWriter;

public interface NashornProcessor {

    void setAndCheck(String script) throws ScriptException;

    void setAndCheck(Reader reader) throws ScriptException;

    void process() throws ScriptException;

    StringWriter getBuffer();

    StringWriter getError();

}
