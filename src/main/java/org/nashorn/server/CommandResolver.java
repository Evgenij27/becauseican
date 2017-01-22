package org.nashorn.server;

import org.nashorn.server.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.regex.*;

public class CommandResolver {

    private static final String TEMPLATE_VAR = "\\{(?<tvar>[a-z]*)\\}";
    private static final String TEMPLATE_REPLACEMENT = "(?<%s>[^/]+)";
    private static final String END_TEMPLATE = "(/)?";

    //private static final String ID_TEMPLATE = "(?<%s>.*)";

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";

    private static final TreeMap<String, TreeMap<String, Command>> COMMANDS =
        new TreeMap<String, TreeMap<String, Command>>();

    static {
        COMMANDS.put(GET, new TreeMap<String, Command>());
        COMMANDS.put(POST, new TreeMap<String, Command>());
        COMMANDS.put(PUT, new TreeMap<String, Command>());
        COMMANDS.put(DELETE, new TreeMap<String, Command>());
    }

    private static String transformTemplate2Path(String commandTemplate) {
        Pattern pattern = Pattern.compile(TEMPLATE_VAR);
        Matcher matcher = pattern.matcher(commandTemplate);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String tvar = matcher.group("tvar");
            matcher.appendReplacement(sb, String.format(TEMPLATE_REPLACEMENT, tvar));
        }
        matcher.appendTail(sb);

        String modifiedString = sb.toString();

        int strLen = sb.length();
        if (sb.charAt(strLen - 1) == '/') {
            sb.deleteCharAt(strLen - 1);

        }
        sb.append(END_TEMPLATE);
        System.out.println("transformed template -> " + sb.toString());
        return sb.toString();
    }

    private static void registerCommand(String method, Command... commands) {
        for (Command c : commands) {
            String path = transformTemplate2Path(c.getPath());
            COMMANDS.get(method).put(path, c);
        }
    }

    public static void registerGet(Command... commands) {
        registerCommand(GET, commands);
    }

    public static void registerPost(Command... commands) {
        registerCommand(POST, commands);
    }

    public static void registerPut(Command... commands) {
        registerCommand(PUT, commands);
    }

    public static void registerDelete(Command... commands) {
        registerCommand(DELETE, commands);
    }

    public Command resolve(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

        System.out.println(COMMANDS);
        Command com = null;
        Pattern pattern = null;
        Matcher matcher = null;

        String uri = request.getRequestURI();
        System.out.println("URI -> " + uri);
        String methodName = request.getMethod();
        System.out.println("Method name -> " + methodName);

        Map<String, Command> comms = COMMANDS.get(methodName);

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
