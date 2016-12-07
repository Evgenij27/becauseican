package org.nashorn;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


public class AsyncListenerImpl implements AsyncListerner {

    private static ThreadPoolExecutor executor;

    public AsyncListenerImpl(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void onComplete(AsyncEvent event) {
        
    }

    @Override
    public void onError(AsyncEvent event) {}

    @Override
    public void onStartAsync(AsyncEvent event) {
        
    }

    @Override
    public void onTimeout(AsyncEvent event) {}


    private void initEvent() {
        
    }
}
