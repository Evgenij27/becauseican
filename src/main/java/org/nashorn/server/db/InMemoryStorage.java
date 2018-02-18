package org.nashorn.server.db;

import org.apache.log4j.Logger;
import org.nashorn.server.core.ExecutionUnit;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryStorage implements DAO {

    private static final Logger LOGGER = Logger.getLogger(InMemoryStorage.class);

    private static final int DEFAULT_CAPACITY = 50;

    private final AtomicLong counter = new AtomicLong();

    private final ConcurrentMap<Long, ExecutionUnit> storage;

    private InMemoryStorage() {
        int capacity = DEFAULT_CAPACITY;
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            capacity = (Integer) envCtx.lookup("storage::capacity");
            LOGGER.info("Injected capacity is: " + capacity);
        } catch (NamingException ex) {
            LOGGER.error("Property not found. Use default value", ex);
        }
        storage = new ConcurrentHashMap<>(capacity);
    }

    public static InMemoryStorage instance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private Holder() {}
        private static final InMemoryStorage INSTANCE = new InMemoryStorage();
    }

    private void checkContainsKeyOrThrow(long id) throws  UnitNotFoundException {
        if (!storage.containsKey(id)) {
            throw new UnitNotFoundException(String.format("Unit with this id %d not found", id));
        }
    }

    @Override
    public long create(ExecutionUnit e) {
        long id = counter.incrementAndGet();
        storage.put(id, e);
        return id;
    }

    @Override
    public ExecutionUnit read(long id) throws UnitNotFoundException {
        checkContainsKeyOrThrow(id);
        return storage.get(id);
    }

    @Override
    public void update(long id, ExecutionUnit e) {
        storage.replace(id, e);
    }

    @Override
    public void delete(long id) throws UnitNotFoundException {
        checkContainsKeyOrThrow(id);
        storage.remove(id);
    }

    @Override
    public Set<ConcurrentMap.Entry<Long, ExecutionUnit>> getAllUnits() {
        return storage.entrySet();
    }
}
