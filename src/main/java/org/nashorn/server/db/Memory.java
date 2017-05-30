package org.nashorn.server.db;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class Memory implements DAO {

    private static final AtomicLong INDEX = new AtomicLong();

    private static final ConcurrentSkipListMap<Long, Future<Result>> TABLE =
            new ConcurrentSkipListMap<>();

    private Memory() {}

    private static Memory instance;

    public static synchronized Memory getInstance() {
        if (instance == null) {
            instance = new Memory();
        }
        return instance;
    }

    @Override
    public long create(Future<Result> result) {
        long id = INDEX.incrementAndGet();
        TABLE.put(id, result);
        return id;
    }

    @Override
    public Future<Result> read(long id) {
        return TABLE.get(id);
    }

    @Override
    public void update(long id, Future<Result> result) {
        TABLE.replace(id, result);
    }

    @Override
    public void delete(long id) {
        TABLE.remove(id);
    }
}
