package org.nashorn.server.db;

import org.nashorn.server.core.Result;

import javax.script.ScriptContext;
import java.util.concurrent.Future;

public interface DAO {

    long create(Future<Result> result);

    Future<Result> read(long id);

    void update(long id, Future<Result> result);

    void delete(long id);
}
