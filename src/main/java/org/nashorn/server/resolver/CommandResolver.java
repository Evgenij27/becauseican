package org.nashorn.server.resolver;

import org.nashorn.server.command.Command;
import org.nashorn.server.CommandNotFoundException;

import javax.servlet.http.HttpServletRequest;

public interface CommandResolver {

    Command resolve(HttpServletRequest req) throws CommandNotFoundException;
}
