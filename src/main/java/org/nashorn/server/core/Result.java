package org.nashorn.server.core;

import java.io.Writer;

public interface Result {

    Writer getResult();
    Writer getError();
}
