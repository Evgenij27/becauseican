package org.nashorn.server.resolver;

import org.apache.log4j.Logger;
import org.nashorn.server.CommandNotFoundException;
import org.nashorn.server.command.Command;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandResolverImpl implements CommandResolver {

    protected static final Logger LOGGER = Logger.getLogger(CommandResolverImpl.class);

    private final String method;

    private final ConcurrentMap<String, Command> endpoints = new ConcurrentSkipListMap<>();

    private CommandResolver next;

    public CommandResolverImpl(String method) {
        this.method = method;
    }

    private Command findCommand(HttpServletRequest request) throws CommandNotFoundException {

        Command com = null;
        Pattern pattern;
        Matcher matcher;

        String uri = request.getRequestURI();
        LOGGER.info("URI " + uri);

        LOGGER.info("COMMANDS " + endpoints);
        for (ConcurrentMap.Entry<String, Command> entry : endpoints.entrySet()) {
            String template = entry.getKey();
            pattern = Pattern.compile(template);
            LOGGER.info("TEMPLATE " + template);
            matcher = pattern.matcher(uri);
            if (matcher.matches()) {
                for (String groupName : getPathVariables(template)) {
                    request.setAttribute(groupName, matcher.group(groupName));
                }
                com = entry.getValue();
            }
        }
        if (com == null) {
            throw new CommandNotFoundException("Endpoint not found");
        }
        return com;
    }

    private List<String> getPathVariables(String template) {
        String groupTemplate = "<\\w+>";
        List<String> pathVariables = new ArrayList<>();
        Pattern pattern = Pattern.compile(groupTemplate);
        Matcher matcher = pattern.matcher(template);

        while (matcher.find()) {
            String groupName = matcher.group();
            pathVariables.add(groupName.substring(1, groupName.length() - 1));
        }
        return pathVariables;
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

    /*
        Register endpoints recursively
     */
    @Override
    public void registerEndpoint(String method, String path, Command command) {
        if (this.method.equalsIgnoreCase(method)) {
            endpoints.put(path, command);
        } else if (this.next == null) {
            this.next = new CommandResolverImpl(method);
            this.next.registerEndpoint(method, path, command);
        } else {
            this.next.registerEndpoint(method, path, command);
        }
    }
}
