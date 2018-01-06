package org.nashorn.server.util.resolver;

import org.nashorn.server.Command;
import org.nashorn.server.CommandNotFoundException;

import javax.servlet.http.HttpServletRequest;

public interface CommandResolver {

    Command resolve(HttpServletRequest req) throws CommandNotFoundException;
}
