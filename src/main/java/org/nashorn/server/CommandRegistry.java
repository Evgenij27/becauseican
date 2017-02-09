package org.nashorn.server;

import org.nashorn.server.command.AnotherCommand;
import org.nashorn.server.command.Command;
import org.nashorn.server.command.TestCommand;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandRegistry {

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

        registerPost(new AnotherCommand());
        registerPost(new TestCommand());
    }

    private static CommandRegistry registry;

    public static synchronized CommandRegistry getRegistry() {
        if (registry == null) {
            registry = new CommandRegistry();
        }
        return registry;
    }

    private CommandRegistry() {}

    public Map<String, Command> getCommandByMethod(String name) {
        return COMMANDS.get(name);
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

    private static void registerGet(Command... commands) {
        registerCommand(GET, commands);
    }

    private static void registerPost(Command... commands) {
        registerCommand(POST, commands);
    }

    private static void registerPut(Command... commands) {
        registerCommand(PUT, commands);
    }

    private static void registerDelete(Command... commands) {
        registerCommand(DELETE, commands);
    }
}
