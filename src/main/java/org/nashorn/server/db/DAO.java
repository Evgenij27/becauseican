package org.nashorn.server.db;


import org.nashorn.server.core.ExecutionUnit;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public interface DAO {

    long create(ExecutionUnit e);

    ExecutionUnit read(long id) throws UnitNotFoundException;

    void update(long id, ExecutionUnit e);

    void delete(long id) throws UnitNotFoundException;

    Set<ConcurrentMap.Entry<Long, ExecutionUnit>> getAllUnits();
}
