package org.nashorn.server.handler.block;

import org.nashorn.server.handler.Handler;
import org.nashorn.server.handler.HandlerBuilder;

public class BlockApiHandlerBuilder extends HandlerBuilder {

    protected BlockApiHandlerBuilder(String rootPath) {
        super(rootPath);
    }

    @Override
    public Handler build() {
        return new BlockApiHandler(this);
    }
}
