package org.nashorn.server.db;


import org.nashorn.server.core.ExecutionUnit;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DAO {

    long create(ExecutionUnit e);

    ExecutionUnit read(long id) throws UnitNotFoundException;

    void update(long id, ExecutionUnit e);

    void delete(long id) throws UnitNotFoundException;

    Set<Map.Entry<Long, ExecutionUnit>> getAllUnits();
}
