package org.nashorn.server;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.regex.*;

public class CommandResolver {

    private static final Logger LOGGER = Logger.getLogger(CommandResolver.class);

    private final ConcurrentMap<String, Command> getEndpoints;
    private final ConcurrentMap<String, Command> postEndpoints;
    private final ConcurrentMap<String, Command> putEndpoints;
    private final ConcurrentMap<String, Command> deleteEndpoints;

    public CommandResolver(ConcurrentMap<String, Command> getEndpoints,
                           ConcurrentMap<String, Command> postEndpoints,
                           ConcurrentMap<String, Command> putEndpoints,
                           ConcurrentMap<String, Command> deleteEndpoints) {
        this.getEndpoints = new ConcurrentSkipListMap<>(getEndpoints);
        this.postEndpoints = new ConcurrentSkipListMap<>(postEndpoints);
        this.putEndpoints = new ConcurrentSkipListMap<>(putEndpoints);
        this.deleteEndpoints = new ConcurrentSkipListMap<>(deleteEndpoints);

    }

    public Command resolve(HttpServletRequest request) throws ServletException {

        Command com = null;
        Pattern pattern = null;
        Matcher matcher = null;

        String uri = request.getRequestURI();
        LOGGER.info("URI " + uri);
        String methodName = request.getMethod();
        LOGGER.info("Method name -> " + methodName);

        ConcurrentMap<String, Command> comms = getCommandByMethod(methodName);
        if (comms == null) {
            throw new ServletException("No such method");
        }
        LOGGER.info("COMMANDS " + comms);
        for (ConcurrentMap.Entry<String, Command> entry : comms.entrySet()) {
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
            throw new ServletException();
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

    private ConcurrentMap<String, Command> getCommandByMethod(String methodName) {
        ConcurrentMap<String, Command> r = null;
        if (methodName.equals("GET")) {
            r = getEndpoints;
        }

        if (methodName.equals("POST")) {
            r = postEndpoints;
        }

        if (methodName.equals("PUT")) {
            r = putEndpoints;
        }

        if (methodName.equals("DELETE")) {
            r = deleteEndpoints;
        }
        return r;
    }
}
