package org.nashorn.server.handler;

import org.apache.log4j.Logger;
import org.nashorn.server.resolver.CommandResolver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class AbstractHandler implements Handler {

    protected static final Logger LOGGER = Logger.getLogger(AbstractHandler.class);

    private static final String PATH_REGEX = "(/([^/]+)(/)?)+?";

    protected final CommandResolver resolverChain;

    protected final String rootPath;

    protected boolean findMatch(String rootPath, String uri) {
        Pattern pattern = Pattern.compile(rootPath + PATH_REGEX);
        Matcher matcher = pattern.matcher(uri);
        return matcher.matches();
    }

    protected AbstractHandler(HandlerBuilder builder) {
        this.resolverChain = builder.resolverChain;
        this.rootPath = builder.rootPath;
    }
}
