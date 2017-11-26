package org.nashorn.server;

import java.util.Map;

public interface Registry {

    Map<String, Command> getRegistry();
    Map<String, Command> postRegistry();
    Map<String, Command> putRegistry();
    Map<String, Command> deleteRegistry();
}
