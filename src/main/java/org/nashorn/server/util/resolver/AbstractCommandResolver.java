package org.nashorn.server.util.resolver;

import org.apache.log4j.Logger;
import org.nashorn.server.Command;
import org.nashorn.server.CommandNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.*;

public abstract class AbstractCommandResolver implements CommandResolver {

    protected static final Logger LOGGER = Logger.getLogger(AbstractCommandResolver.class);

    protected final ConcurrentMap<String, Command> endpoints;

    protected AbstractCommandResolver(ConcurrentMap<String, Command> endpoints) {
        this.endpoints = endpoints;
    }

    protected Command findCommand(HttpServletRequest request) throws CommandNotFoundException {

        Command com = null;
        Pattern pattern = null;
        Matcher matcher = null;

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
}
