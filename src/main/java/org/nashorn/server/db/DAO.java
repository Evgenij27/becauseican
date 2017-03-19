package org.nashorn.server.db;

import org.nashorn.server.Bucket;

public interface DAO {

    long create(Bucket bucket);

    Bucket read(long id);

    void update(long id, Bucket bucket);

    void delete(long id);
}
