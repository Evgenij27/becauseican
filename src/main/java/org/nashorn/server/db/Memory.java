package org.nashorn.server.db;

import org.nashorn.server.exception.AppException;
import org.nashorn.server.factory.ScriptTask;
import org.nashorn.server.factory.TaskFactory;

import javax.script.ScriptException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class Memory {

    private static final AtomicLong idCounter = new AtomicLong();

    private static Memory instance;

    public static synchronized Memory getInstance() {
        if (instance == null) {
            instance = new Memory();
        }
        return instance;
    }

    private static final ThreadPoolExecutor POOL = new ThreadPoolExecutor(
            10,
            20,
            0,
            TimeUnit.NANOSECONDS,
            new ArrayBlockingQueue<Runnable>(100));
    ;

    private static final ConcurrentSkipListMap<Long, ScriptTask> MAP =
            new ConcurrentSkipListMap<>();

    private Memory() {}

    public long create(ScriptTask task) throws AppException {
        long id = incrementId();
        POOL.submit(task);
        MAP.put(id, task);
        return id;
    }

    public boolean delete(long id) {
        ScriptTask task = MAP.remove(id);
        task.cancel();
        decrementId();
        return POOL.remove(task);
    }

    public ScriptTask read(long id) {
        return MAP.get(id);
    }

    public void update(long id, ScriptTask newTask) {
        MAP.replace(id, newTask);
        POOL.submit(newTask);
    }

    private long incrementId() {
        return idCounter.getAndIncrement();
    }

    private long decrementId() {
        return idCounter.getAndDecrement();
    }
}
