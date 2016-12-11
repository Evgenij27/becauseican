package org.nashorn;

import javax.servlet.AsyncContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.GenericServlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

@WebServlet(name = "home", urlPatterns ={"/*"}, asyncSupported = true)
public class Home extends GenericServlet {

    private static int QUEUE_CAPACITY;

    private static BlockingQueue<Runnable> QUEUE;
    
    private static ThreadPoolExecutor EXECUTOR;

    private static int CORE_POOL_SIZE;

    private static int MAX_POOL_SIZE;

    private static long AWAIT_TIME;

    @Override
    public void init(ServletConfig config) throws ServletException {
        QUEUE_CAPACITY = 10;
        CORE_POOL_SIZE = 3;
        MAX_POOL_SIZE = 5;
        AWAIT_TIME = 0L;
        QUEUE = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        EXECUTOR =
            new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, AWAIT_TIME, TimeUnit.MILLISECONDS, QUEUE);
    }

    @Override
    public void service(ServletRequest req, ServletResponse resp)
        throws IOException, ServletException {

        System.out.println("SERVICE");
        AsyncContext actx = req.startAsync();
        actx.addListener(new AsyncListenerImpl(EXECUTOR));
        actx.start(new Running());
    }
}
