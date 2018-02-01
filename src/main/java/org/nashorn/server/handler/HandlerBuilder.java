package org.nashorn.server.handler;

import org.nashorn.server.command.Command;
import org.nashorn.server.util.PathTransformer;
import org.nashorn.server.util.RequestPathTransformer;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

public abstract class HandlerBuilder implements Builder<Handler> {

    private final PathTransformer transformer;

    protected String rootPath;

    protected ConcurrentMap<String, Command> getEndpoints = new ConcurrentSkipListMap<>();
    protected ConcurrentMap<String, Command> postEndpoints = new ConcurrentSkipListMap<>();
    protected ConcurrentMap<String, Command> putEndpoints = new ConcurrentSkipListMap<>();
    protected ConcurrentMap<String, Command> deleteEndpoints = new ConcurrentSkipListMap<>();

    protected HandlerBuilder(String rootPath) {
        this.rootPath = rootPath;
        this.transformer = new RequestPathTransformer(rootPath);
    }

    public HandlerBuilder getEndpoint(String path, Command command) {
        getEndpoints.put(transformer.transform(path), command);
        return this;
    }

    public HandlerBuilder postEndpoint(String path, Command command) {
        postEndpoints.put(transformer.transform(path), command);
        return this;
    }

    public HandlerBuilder putEndpoint(String path, Command command) {
        putEndpoints.put(transformer.transform(path), command);
        return this;
    }

    public HandlerBuilder deleteEndpoint(String path, Command command) {
        deleteEndpoints.put(transformer.transform(path), command);
        return this;
    }
}
