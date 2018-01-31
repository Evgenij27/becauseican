package org.nashorn.server.handler;

import org.nashorn.server.HttpRequestEntity;
import org.nashorn.server.HttpResponseEntity;

import javax.servlet.ServletException;

public class HandlerChainImpl implements HandlerChain {

    private final Handler handler;
    private final HandlerChain chain;

    public HandlerChainImpl(Handler handler, HandlerChain chain) {
        this.handler = handler;
        this.chain = chain;
    }

    @Override
    public void handle(HttpRequestEntity req, HttpResponseEntity resp) throws ServletException {
        handler.handle(req, resp, chain);
    }
}
