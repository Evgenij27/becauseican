package org.nashorn.server.core;

import javax.script.ScriptException;
import java.io.Reader;
import java.io.StringWriter;

public interface NashornProcessor {

    void process() throws ScriptException;

}
