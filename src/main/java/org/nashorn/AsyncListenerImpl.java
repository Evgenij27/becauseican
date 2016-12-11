package org.nashorn;

import java.io.IOException;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletInputStream;

import java.util.concurrent.ThreadPoolExecutor;

public class AsyncListenerImpl implements AsyncListener {

    private static ThreadPoolExecutor executor;

    public AsyncListenerImpl(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void onComplete(AsyncEvent event) {
        System.out.println("onComplete");
    }

    @Override
    public void onError(AsyncEvent event) {
        System.out.println("onError");
    }

    @Override
    public void onStartAsync(AsyncEvent event) {
        event.getAsyncContext().addListener(this);
        System.out.println("onStartAsync");
        initEvent(event);
    }

    @Override
    public void onTimeout(AsyncEvent event) {
        System.out.println("onTimeout");
    }


    private void initEvent(AsyncEvent event) {
        addRequestReadListener(event.getSuppliedRequest());
        addResponseWriteListener(event.getSuppliedResponse());
    }

    private void addRequestReadListener(ServletRequest request) {
        ServletInputStream input = getServletInputStream(request);
        RequestReadListener requestListener = new RequestReadListener(input, new StringBuilder());
        input.setReadListener(requestListener);
    }

    private void addResponseWriteListener(ServletResponse response) {
        ServletOutputStream output = getServletOutputStream(response);
        ResponseWriteListener responseListener =
            new ResponseWriteListener(output, new StringBuilder("Hello World"));
        output.setWriteListener(responseListener);
    }

    private ServletInputStream getServletInputStream(ServletRequest request) {
        ServletInputStream input = null;
        try {
            input = request.getInputStream();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return input;
    }

    private ServletOutputStream getServletOutputStream(ServletResponse response) {
        ServletOutputStream output = null;
        try {
            output = response.getOutputStream();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return output;
    }
}
