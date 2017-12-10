package org.nashorn.server;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.regex.*;

public class CommandResolver {

    private final Logger logger = Logger.getLogger(CommandResolver.class);

    private final ConcurrentSkipListMap<String, Command> getEndpoints;
    private final ConcurrentSkipListMap<String, Command> postEndpoints;
    private final ConcurrentSkipListMap<String, Command> putEndpoints;
    private final ConcurrentSkipListMap<String, Command> deleteEndpoints;

    public CommandResolver(ConcurrentSkipListMap<String, Command> getEndpoints,
                           ConcurrentSkipListMap<String, Command> postEndpoints,
                           ConcurrentSkipListMap<String, Command> putEndpoints,
                           ConcurrentSkipListMap<String, Command> deleteEndpoints) {
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
        System.out.println("URI -> " + uri);
        logger.info("URI " + uri);
        String methodName = request.getMethod();
        System.out.println("Method name -> " + methodName);

        Map<String, Command> comms = getCommandByMethod(methodName);
        logger.info("COMMANDS " + comms);
        for (Map.Entry<String, Command> entry : comms.entrySet()) {
            String template = entry.getKey();
            pattern = Pattern.compile(template);
            logger.info("TEMPLATE " + template);
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

    private Map<String, Command> getCommandByMethod(String methodName) {
        Map<String, Command> r = null;
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
