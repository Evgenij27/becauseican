package org.nashorn.server;

public interface Router {

    void get(String pathTemplate, Command command);
    void post(String pathTemplate, Command command);
    void put(String pathTemplate, Command command);
    void delete(String pathTemplate, Command command);
}
