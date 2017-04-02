package org.nashorn.server.db;

import org.nashorn.server.DeferredResult;

public interface DAO {

    long create(DeferredResult result);

    DeferredResult read(long id);

    void update(long id, DeferredResult result);

    void delete(long id);
}
