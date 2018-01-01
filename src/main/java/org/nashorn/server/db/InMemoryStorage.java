package org.nashorn.server.db;

import org.nashorn.server.core.ExecutionUnit;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryStorage implements DAO {

    private final AtomicLong counter = new AtomicLong();

    private final ConcurrentHashMap<Long, ExecutionUnit> storage = new ConcurrentHashMap<>();

    private InMemoryStorage() {}

    public static InMemoryStorage instance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private Holder() {}
        private static final InMemoryStorage INSTANCE = new InMemoryStorage();
    }

    @Override
    public long create(ExecutionUnit e) {
        long id = counter.incrementAndGet();
        storage.put(id, e);
        return id;
    }

    @Override
    public ExecutionUnit read(long id) {
        return storage.get(id);
    }

    @Override
    public void update(long id, ExecutionUnit e) {
        storage.replace(id, e);
    }

    @Override
    public void delete(long id) {
        storage.remove(id);
    }

    @Override
    public Set<Map.Entry<Long, ExecutionUnit>> getAllUnits() {
        return storage.entrySet();
    }
}
