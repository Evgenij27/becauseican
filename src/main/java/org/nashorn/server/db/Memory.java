package org.nashorn.server.db;

import org.nashorn.server.Bucket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class Memory implements DAO {

    private static final AtomicLong INDEX = new AtomicLong();

    private static final ConcurrentSkipListMap<Long, Bucket> TABLE = new ConcurrentSkipListMap<>();

    private Memory() {}

    public static final Memory INSTANCE = new Memory();

    @Override
    public long create(Bucket bucket) {
        long id = INDEX.getAndIncrement();
        TABLE.put(id, bucket);
        return id;
    }

    @Override
    public Bucket read(long id) {
        return TABLE.get(id);
    }

    @Override
    public void update(long id, Bucket bucket) {
        TABLE.replace(id, bucket);
    }

    @Override
    public void delete(long id) {
        TABLE.remove(id);
    }
}