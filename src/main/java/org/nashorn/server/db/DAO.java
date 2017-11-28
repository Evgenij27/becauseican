package org.nashorn.server.db;


import org.nashorn.server.core.ExecutionUnit;

public interface DAO {

    long create(ExecutionUnit e);

    ExecutionUnit read(long id);

    void update(long id, ExecutionUnit e);

    void delete(long id);
}
