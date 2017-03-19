package org.nashorn.server.factory;

import javax.script.ScriptException;

public interface TaskFactory {

    Runnable newTask(String code) throws ScriptException;
}
