package org.nashorn.server.core;

import javax.script.ScriptException;

public interface NashornProcessor {

    Result process() throws ScriptException;


}
