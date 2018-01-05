package org.nashorn.server.handler;

import org.nashorn.server.CommandResolver;


public abstract class AbstractHandler implements Handler {

    protected final CommandResolver resolver;

    protected final String rootPath;

    protected AbstractHandler(HandlerBuilder builder) {
        this.resolver = new CommandResolver(builder.getEndpoints, builder.postEndpoints,
                builder.putEndpoints, builder.deleteEndpoints);
        this.rootPath = builder.rootPath;
    }

    public String getRootPath() {
        return this.rootPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractHandler that = (AbstractHandler) o;

        return rootPath != null ? rootPath.equals(that.rootPath) : that.rootPath == null;
    }

    @Override
    public int hashCode() {
        return rootPath != null ? rootPath.hashCode() : 0;
    }

    @Override
    public int compareTo(Handler o) {
        AbstractHandler ah = (AbstractHandler) o;
        return ah.getRootPath().compareTo(getRootPath());
    }


}
