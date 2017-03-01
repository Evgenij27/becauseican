package org.nashorn.server;

import org.nashorn.server.command.Command;

public class ClassMetadata {

    private final Class<?> commandClass;
    private final String path;
    private final String methodName;

    public ClassMetadata(Class<?> commandClass, String path, String methodName) {
        this.commandClass = commandClass;
        this.path = path;
        this.methodName = methodName;
    }

    public Class<?> getCommandClass() {
        return commandClass;
    }

    public String getPath() {
        return path;
    }

    public String getMethodName() {
        return methodName;
    }
}
