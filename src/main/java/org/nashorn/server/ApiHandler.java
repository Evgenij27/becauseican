package org.nashorn.server;

import org.apache.log4j.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentSkipListMap;

public class ApiHandler implements Handler {

    private static final Logger LOGGER = Logger.getLogger(ApiHandler.class);

    private final CommandResolver resolver;

    private final String rootPath;

    private ApiHandler(Builder b) {
        this.resolver = new CommandResolver(b.getEndpoints, b.postEndpoints, b.putEndpoints,
                b.deleteEndpoints);
        this.rootPath = b.rootPath;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Command command = null;
        command = resolver.resolve(request);
        command.execute(request, response);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApiHandler that = (ApiHandler) o;

        return rootPath.equals(that.rootPath);
    }

    @Override
    public int hashCode() {
        return rootPath.hashCode();
    }

    public String getRootPath() {
        return rootPath;
    }

    @Override
    public int compareTo(Handler o) {
        ApiHandler ah = (ApiHandler) o;
        return ah.rootPath.compareTo(this.rootPath);
    }

    public static class Builder {

        private final PathTransformer transformer;

        private String rootPath;

        private ConcurrentSkipListMap<String, Command> getEndpoints = new ConcurrentSkipListMap<>();
        private ConcurrentSkipListMap<String, Command> postEndpoints = new ConcurrentSkipListMap<>();
        private ConcurrentSkipListMap<String, Command> putEndpoints = new ConcurrentSkipListMap<>();
        private ConcurrentSkipListMap<String, Command> deleteEndpoints = new ConcurrentSkipListMap<>();

        public Builder(String rootPath) {
            this.rootPath = rootPath;
            this.transformer = new RequestPathTransformer(rootPath);
        }

        public Builder() {
            this("");
        }

        public Builder registerGetEndpoint(String path, Command command) {
            LOGGER.info("GET Register : " + path);
            getEndpoints.put(transformer.transform(path), command);
            LOGGER.info("End GET Register");
            return this;
        }

        public Builder registerPostEndpoint(String path, Command command) {
            postEndpoints.put(transformer.transform(path), command);
            return this;
        }

        public Builder registerPutEndpoint(String path, Command command) {
            putEndpoints.put(transformer.transform(path), command);
            return this;
        }

        public Builder registerDeleteEndpoint(String path, Command command) {
            deleteEndpoints.put(transformer.transform(path), command);
            return this;
        }

        public ApiHandler build() {
            return new ApiHandler(this);
        }
    }

}
