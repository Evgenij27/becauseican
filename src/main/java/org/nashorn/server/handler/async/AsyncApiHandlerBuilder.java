package org.nashorn.server.handler.async;

import org.nashorn.server.handler.Handler;
import org.nashorn.server.handler.HandlerBuilder;

public class AsyncApiHandlerBuilder extends HandlerBuilder {

    public AsyncApiHandlerBuilder(String rootPath) {
        super(rootPath);
    }

    @Override
    public Handler build() {
        return new AsyncApiHandler(this);
    }
}
