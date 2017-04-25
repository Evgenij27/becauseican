package org.nashorn.server.core;

import javax.script.ScriptException;

public interface NashornProcessor {

    Result eval() throws ScriptException;


}
