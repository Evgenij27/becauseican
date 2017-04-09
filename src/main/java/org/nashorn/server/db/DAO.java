package org.nashorn.server.db;

import javax.script.ScriptContext;
import java.util.concurrent.Future;

public interface DAO {

    long create(Future<ScriptContext> result);

    Future<ScriptContext> read(long id);

    void update(long id, Future<ScriptContext> result);

    void delete(long id);
}
