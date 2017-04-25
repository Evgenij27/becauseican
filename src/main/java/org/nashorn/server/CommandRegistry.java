package org.nashorn.server;

import org.nashorn.server.annotation.DeleteMapping;
import org.nashorn.server.annotation.GetMapping;
import org.nashorn.server.annotation.PostMapping;
import org.nashorn.server.annotation.PutMapping;
import org.nashorn.server.command.Command;


import java.io.*;
import java.lang.annotation.Annotation;

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
        COMMANDS.put(GET, new TreeMap<>());
        COMMANDS.put(POST, new TreeMap<>());
        COMMANDS.put(PUT, new TreeMap<>());
        COMMANDS.put(DELETE, new TreeMap<>());

        scan();

        System.out.println(COMMANDS);

    }

    private static CommandRegistry registry;

    public static synchronized CommandRegistry getRegistry() {
        if (registry == null) {
            System.out.println("COMMAND REGISTRY : " + CommandRegistry.class);
            registry = new CommandRegistry();
        }
        return registry;
    }

    private CommandRegistry() {
    }

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

    private static void registerCommand(List<ClassMetadata> classMetaList) {
        for (ClassMetadata metadata : classMetaList) {
            String method = metadata.getMethodName();
            String path = transformTemplate2Path(metadata.getPath());
            Command command = makeInstance(metadata.getCommandClass());
            COMMANDS.get(method).put(path, command);
        }
    }

    private static void registerGet(Command... commands) {

    }

    private static void registerPost(Command... commands) {

    }

    private static void registerPut(Command... commands) {

    }

    private static void registerDelete(Command... commands) {

    }

    private static void scan() {
        String pathSeparator = System.getProperty("file.separator");
        List<String> commandNameList = new ArrayList<>();

        Properties prop = loadProps("config.properties");
        String packageCommand = prop.getProperty("package.command").replace(".", "/");
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String url = loader.getResource(packageCommand).getPath();
        System.out.println("PATH : " + url);

        processFile(url, packageCommand, commandNameList);

        System.out.println("Command Name List : " + commandNameList);

        List<ClassMetadata> metadataList = getClassMeta(commandNameList);

        System.out.println("Metadata list : " + metadataList);

        registerCommand(metadataList);

    }

    private static void processFile(String path, String commandPackagePath, List<String> commandNamesList) {
        System.out.println("START RECURSION " + path);
        File[] files = new File(path).listFiles();
        for (File file : files) {
            String filePath = file.getPath();
            if (file.isDirectory()) {
                System.out.println("Directory path " + filePath);
                processFile(filePath, commandPackagePath, commandNamesList);
            }
            commandNamesList.add(findFQN(filePath, commandPackagePath));
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

    private static List<ClassMetadata> getClassMeta(List<String> commandNamesList) {
        System.out.println("getClassMeta sizeOf commandNamesList : " + commandNamesList.size());
        List<ClassMetadata> classMetaList = new ArrayList<>();

        for (String commandName : commandNamesList) {
            System.out.println("getClassMeta cycle");
            try {
                System.out.println("getClassMeta try");
                Class<?> commandClass = Class.forName(commandName);
                if (!commandClass.isAnnotation()) {
                    Annotation[] annotations = commandClass.getAnnotations();
                    System.out.println(annotations.length);
                    for (Annotation annotation : annotations) {
                        if (annotation.annotationType() == PostMapping.class) {
                            PostMapping pm = (PostMapping) annotation;
                            classMetaList.add(new ClassMetadata(commandClass, pm.path(), "POST"));
                        } else if (annotation.annotationType() == GetMapping.class) {
                            GetMapping gm = (GetMapping) annotation;
                            classMetaList.add(new ClassMetadata(commandClass, gm.path(), "GET"));
                        } else if (annotation.annotationType() == DeleteMapping.class) {
                            DeleteMapping dm = (DeleteMapping) annotation;
                            classMetaList.add(new ClassMetadata(commandClass, dm.path(), "DELETE"));
                        } else if (annotation.annotationType() == PutMapping.class) {
                            PutMapping putMapping = (PutMapping) annotation;
                            classMetaList.add(new ClassMetadata(commandClass, putMapping.path(), "PUT"));
                        }
                    }
                    //classMetaList.add(new ClassMetadata(commandClass, pathAnnotation.path(), requestAnnotation.toString()));
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        return classMetaList;
    }

    private static Command makeInstance(Class<?> clazz) {
        Command command = null;
        try {
            command = (Command) clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return command;
    }
}

