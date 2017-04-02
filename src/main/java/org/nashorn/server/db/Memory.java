package org.nashorn.server.db;

import org.nashorn.server.DeferredResult;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class Memory implements DAO {

    private static final AtomicLong INDEX = new AtomicLong();

    private static final ConcurrentSkipListMap<Long, DeferredResult> TABLE =
            new ConcurrentSkipListMap<>();

    private Memory() {}

    public static final Memory INSTANCE = new Memory();

    @Override
    public long create(DeferredResult result) {
        long id = INDEX.getAndIncrement();
        TABLE.put(id, result);
        return id;
    }

    @Override
    public DeferredResult read(long id) {
        return TABLE.get(id);
    }

    @Override
    public void update(long id, DeferredResult result) {
        TABLE.replace(id, result);
    }

    @Override
    public void delete(long id) {
        TABLE.remove(id);
    }
}