package org.nashorn.server;

import org.nashorn.server.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.*;


public class CommandResolver {

    private static final String TEMPLATE = "\\{([a-z]*)\\}";

    private static final String ID_TEMPLATE = "(?<%s>.*)/*";
    private static final String ALL_TEMPLATE = "([^/]+?)";
    private static final String END_TEMPLATE = "(/.*)?";

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
        Pattern pattern = Pattern.compile(TEMPLATE);
        Matcher matcher = pattern.matcher(commandTemplate);
        StringBuffer sb = new StringBuffer();
        int i = 0;

        while (matcher.find()) {
            String templateName = matcher.group(++i);
            matcher.appendReplacement(sb, String.format(ID_TEMPLATE, templateName));
        }
        matcher.appendTail(sb);
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
            pattern = Pattern.compile(entry.getKey());
            matcher = pattern.matcher(uri);
            if (matcher.find()) {
                com = entry.getValue();
            }
        }
        return com;
    }


}
