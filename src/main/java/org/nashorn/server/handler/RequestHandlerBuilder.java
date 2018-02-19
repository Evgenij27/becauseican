package org.nashorn.server.handler;

import org.apache.log4j.Logger;
import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.CommandNotFoundException;
import org.nashorn.server.HttpRequestEntity;
import org.nashorn.server.HttpResponsePublisher;
import org.nashorn.server.command.Command;
import org.nashorn.server.resolver.CommandResolver;
import org.nashorn.server.resolver.CommandResolverImpl;
import org.nashorn.server.resolver.HttpMethod;
import org.nashorn.server.util.PathTransformer;
import org.nashorn.server.util.RequestPathTransformer;

import javax.servlet.ServletException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RequestHandlerBuilder implements Builder<Handler> {

    private static final Logger LOGGER = Logger.getLogger(RequestHandlerBuilder.class);

    private static final String PATH_REGEX = "(/([^/]+)(/)?)+?";

    private final PathTransformer transformer;
    private final String rootPath;
    private final CommandResolver resolverChain = new CommandResolverImpl(HttpMethod.GET);

    public RequestHandlerBuilder(String rootPath) {
        this.rootPath = rootPath;
        this.transformer = new RequestPathTransformer(rootPath);
    }

    public RequestHandlerBuilder getEndpoint(String path, Command command) {
        resolverChain.registerEndpoint(HttpMethod.GET, transformer.transform(path), command);
        return this;
    }

    public RequestHandlerBuilder postEndpoint(String path, Command command) {
        resolverChain.registerEndpoint(HttpMethod.POST, transformer.transform(path), command);
        return this;
    }

    public RequestHandlerBuilder putEndpoint(String path, Command command) {
        resolverChain.registerEndpoint(HttpMethod.PUT, transformer.transform(path), command);
        return this;
    }

    public RequestHandlerBuilder deleteEndpoint(String path, Command command) {
        resolverChain.registerEndpoint(HttpMethod.DELETE, transformer.transform(path), command);
        return this;
    }

    @Override
    public Handler build() {
        return new RequestHandler(this);
    }

    private class RequestHandler implements Handler {

        private final CommandResolver resolverChain;

        private final String rootPath;

        private boolean findMatch(String rootPath, String uri) {
            Pattern pattern = Pattern.compile(rootPath + PATH_REGEX);
            Matcher matcher = pattern.matcher(uri);
            return matcher.matches();
        }

        private RequestHandler(RequestHandlerBuilder builder) {
            this.resolverChain = builder.resolverChain;
            this.rootPath = builder.rootPath;
        }

        @Override
        public void handle(HttpRequestEntity req, HttpResponsePublisher pub, HandlerChain chain)
                throws ServletException {
            if (findMatch(rootPath, req.getRequestURI())) {
                try {
                    Command command = resolverChain.resolve(req);
                    command.execute(req, pub);
                } catch (CommandNotFoundException | CommandExecutionException ex) {
                    LOGGER.error(ex.getMessage());
                    pub.statusBadRequest().content(ex.getMessage()).publish();

                }
            } else if (chain != null) {
                chain.handle(req, pub);
            } else {
                pub.statusBadRequest()
                        .content("Bad request. Such type of API is not supported.")
                        .publish();
            }
        }
    }
}
