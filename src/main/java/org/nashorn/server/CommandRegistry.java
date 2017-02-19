package org.nashorn.server;

import org.nashorn.server.command.AnotherCommand;
import org.nashorn.server.command.Command;
import org.nashorn.server.command.TestCommand;

import java.io.*;
import java.net.URL;

import java.util.*;
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

        findCommands();

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

    private static List<Command> findCommands() {
        String pathSeparator = System.getProperty("file.separator");
        List<Command> commands = new ArrayList<>();

        Properties prop = loadProps("config.properties");
        String packageCommand = prop.getProperty("package.command").replace(".", "/");
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String url = loader.getResource(packageCommand).getPath();
        System.out.println("PATH : " + url);

        processFile(url, packageCommand);

        return commands;
      }

    private static void processFile(String path, String commandPackagePath) {
        System.out.println("START RECURSION " + path);
        File[] files = new File(path).listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println("Directory path " + file.getPath());
                StringBuilder pathBuilder = new StringBuilder();
                pathBuilder.append(file.getPath());
                processFile(pathBuilder.toString(), commandPackagePath);
            }
            System.out.println(findFQN(file.getPath(), commandPackagePath));
            }
    }

    private static String findFQN(String path, String commandPackage) {
        int commandPackageStartIndex = path.indexOf(commandPackage);
        int extensionIndex = path.lastIndexOf(".");
        String fqn = path.substring(commandPackageStartIndex, extensionIndex).replace("/", ".");
        System.out.println("fqn = " + fqn);
        return fqn;
    }



    private static Properties loadProps(String fileName) {
        Properties props = new Properties();
        InputStream input = null;

        try {
            input = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (input == null) {
                System.out.println("Can not find file " + fileName);
            } else {
                props.load(input);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } finally {
            close(input);
        }
        return props;
    }

    private static void close(InputStream input) {
        if (input != null) {
            try {
                input.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
