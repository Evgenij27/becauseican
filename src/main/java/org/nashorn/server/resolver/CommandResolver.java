package org.nashorn.server.resolver;

import org.nashorn.server.CommandNotFoundException;
import org.nashorn.server.command.Command;

import javax.servlet.http.HttpServletRequest;

public interface CommandResolver {

    void registerEndpoint(String method, String path, Command command);

    Command resolve(HttpServletRequest req) throws CommandNotFoundException;
}
