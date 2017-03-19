package org.nashorn.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Handler {

    private static final ExecutorService SERVICE = Executors.newFixedThreadPool(10);

    private Handler() {}

    public static final Handler INSTANCE = new Handler();

    public Future handle(Runnable task) {
        return SERVICE.submit(task);
    }

}
