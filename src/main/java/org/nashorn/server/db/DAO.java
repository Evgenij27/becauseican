package org.nashorn.server.db;

import java.util.concurrent.Future;

public interface DAO {

    long create(Future<Result> result);

    Future<Result> read(long id);

    void update(long id, Future<Result> result);

    void delete(long id);
}
