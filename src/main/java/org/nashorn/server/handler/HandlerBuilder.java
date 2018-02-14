package org.nashorn.server.handler;

import org.nashorn.server.Builder;
import org.nashorn.server.command.Command;
import org.nashorn.server.resolver.CommandResolver;
import org.nashorn.server.resolver.CommandResolverImpl;
import org.nashorn.server.resolver.HttpMethod;
import org.nashorn.server.util.PathTransformer;
import org.nashorn.server.util.RequestPathTransformer;


public abstract class HandlerBuilder implements Builder<Handler> {
    private final PathTransformer transformer;
    protected final String rootPath;
    protected final CommandResolver resolverChain = new CommandResolverImpl(HttpMethod.GET);

    public HandlerBuilder(String rootPath) {
        this.rootPath = rootPath;
        this.transformer = new RequestPathTransformer(rootPath);
    }

    public HandlerBuilder getEndpoint(String path, Command command) {
        resolverChain.registerEndpoint(HttpMethod.GET, transformer.transform(path), command);
        return this;
    }

    public HandlerBuilder postEndpoint(String path, Command command) {
        resolverChain.registerEndpoint(HttpMethod.POST, transformer.transform(path), command);
        return this;
    }

    public HandlerBuilder putEndpoint(String path, Command command) {
        resolverChain.registerEndpoint(HttpMethod.PUT, transformer.transform(path), command);
        return this;
    }

    public HandlerBuilder deleteEndpoint(String path, Command command) {
        resolverChain.registerEndpoint(HttpMethod.DELETE, transformer.transform(path), command);
        return this;
    }
}
