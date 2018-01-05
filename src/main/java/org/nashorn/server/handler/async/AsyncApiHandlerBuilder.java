package org.nashorn.server.handler.async;

import org.nashorn.server.handler.HandlerBuilder;
import org.nashorn.server.handler.Handler;

public class AsyncApiHandlerBuilder extends HandlerBuilder {

    protected AsyncApiHandlerBuilder(String rootPath) {
        super(rootPath);
    }

    @Override
    public Handler build() {
        return new AsyncApiHandler(this);
    }
}
