package org.nashorn.server;

import org.nashorn.server.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.*;

public class CommandResolver {

    private static CommandRegistry registry;

    public CommandResolver(CommandRegistry registry) {
        CommandResolver.registry = registry;
    }

    public Command resolve(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

        Command com = null;
        Pattern pattern = null;
        Matcher matcher = null;

        String uri = request.getRequestURI();
        System.out.println("URI -> " + uri);
        String methodName = request.getMethod();
        System.out.println("Method name -> " + methodName);

        Map<String, Command> comms = registry.getCommandByMethod(methodName);

        for (Map.Entry<String, Command> entry : comms.entrySet()) {
            String template = entry.getKey();
            pattern = Pattern.compile(template);
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
}
