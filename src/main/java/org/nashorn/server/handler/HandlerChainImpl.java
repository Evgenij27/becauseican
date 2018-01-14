package org.nashorn.server.handler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerChainImpl implements HandlerChain {

    private final Handler handler;
    private final HandlerChain chain;

    public HandlerChainImpl(Handler handler, HandlerChain chain) {
        this.handler = handler;
        this.chain = chain;
    }

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        handler.handle(req, resp, chain);
    }
}
