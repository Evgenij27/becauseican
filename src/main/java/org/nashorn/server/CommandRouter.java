package org.nashorn.server;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class CommandRouter implements Router, Registrable {

    private final PathTransformer transformer = new RequestPathTransformer();

    private final Map<String, Command> GET_REGISTRY    = new ConcurrentSkipListMap<>();
    private final Map<String, Command> POST_REGISTRY   = new ConcurrentSkipListMap<>();
    private final Map<String, Command> PUT_REGISTRY    = new ConcurrentSkipListMap<>();
    private final Map<String, Command> DELETE_REGISTRY = new ConcurrentSkipListMap<>();

    private final String rootPath;

    public CommandRouter() {
        this("");
    }

    public CommandRouter(String rootPath) {
        this.rootPath = rootPath;
    }

    private String transformRequestPathTemplate(String template) {
        return transformer.transform(rootPath + template);
    }

    @Override
    public void get(String pathTemplate, Command command) {
        GET_REGISTRY.put(transformRequestPathTemplate(pathTemplate), command);
    }

    @Override
    public void post(String pathTemplate, Command command) {
        POST_REGISTRY.put(transformRequestPathTemplate(pathTemplate), command);
    }

    @Override
    public void put(String pathTemplate, Command command) {
        PUT_REGISTRY.put(transformRequestPathTemplate(pathTemplate), command);
    }

    @Override
    public void delete(String pathTemplate, Command command) {
        DELETE_REGISTRY.put(transformRequestPathTemplate(pathTemplate), command);
    }

    @Override
    public Registry registry() {
        return new CommandRegistry(GET_REGISTRY, POST_REGISTRY, PUT_REGISTRY, DELETE_REGISTRY);
    }

    private class CommandRegistry implements Registry {

        private final Map<String, Command> getCommands;
        private final Map<String, Command> postCommands;
        private final Map<String, Command> putCommands;
        private final Map<String, Command> deleteCommands;

        private CommandRegistry(Map<String, Command> getCommads,
                                Map<String, Command> postCommads,
                                Map<String, Command> putCommads,
                                Map<String, Command> deleteCommads) {

            this.getCommands    = new ConcurrentSkipListMap<>(getCommads);
            this.postCommands   = new ConcurrentSkipListMap<>(postCommads);
            this.putCommands    = new ConcurrentSkipListMap<>(putCommads);
            this.deleteCommands = new ConcurrentSkipListMap<>(deleteCommads);
        }

        @Override
        public Map<String, Command> getRegistry() {
           return new ConcurrentSkipListMap<>(GET_REGISTRY);
        }

        @Override
        public Map<String, Command> postRegistry() {
            return new ConcurrentSkipListMap<>(POST_REGISTRY);
        }

        @Override
        public Map<String, Command> putRegistry() {
            return new ConcurrentSkipListMap<>(PUT_REGISTRY);
        }

        @Override
        public Map<String, Command> deleteRegistry() {
            return new ConcurrentSkipListMap<>(DELETE_REGISTRY);
        }
    }
}
