package org.nashorn.server.resolver;

import org.nashorn.server.command.Command;
import org.nashorn.server.CommandNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ConcurrentMap;

public class CommandResolverImpl extends AbstractCommandResolver {

    private final String method;

    private final CommandResolver next;

    public CommandResolverImpl(ConcurrentMap<String, Command> endpoints, String method, CommandResolver next) {
        super(endpoints);
        this.method = method;
        this.next = next;
    }

    @Override
    public Command resolve(HttpServletRequest req) throws CommandNotFoundException {
        Command command;
        if (req.getMethod().equalsIgnoreCase(method)) {
            command = findCommand(req);
        } else if (next != null) {
            command = next.resolve(req);
        } else {
            throw new CommandNotFoundException("This method does not supported");
        }
        return command;
    }
}
