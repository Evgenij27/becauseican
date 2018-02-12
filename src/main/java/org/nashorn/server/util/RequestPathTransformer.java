package org.nashorn.server.util;

import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestPathTransformer implements PathTransformer {

    private final Logger logger = Logger.getLogger(RequestPathTransformer.class);

    private static final String TEMPLATE_VAR = ":(?<tvar>[a-z]*)";
    private static final String TEMPLATE_REPLACEMENT = "(?<%s>[^/]+)";
    private static final String END_TEMPLATE = "(/)?";

    private final String rootPath;

    public RequestPathTransformer(String rootPath) {
        this.rootPath = rootPath;
    }

    public RequestPathTransformer() {
        this("");
    }

    @Override
    public String transform(String template) {
        logger.info("Start transformation");
        Pattern pattern = Pattern.compile(TEMPLATE_VAR);
        Matcher matcher = pattern.matcher(rootPath + template);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String tvar = matcher.group("tvar");
            matcher.appendReplacement(sb, String.format(TEMPLATE_REPLACEMENT, tvar));
        }
        matcher.appendTail(sb);

        int lastChar = sb.length() - 1;
        if (sb.charAt(lastChar) == '/') {
            sb.deleteCharAt(lastChar);

        }
        sb.append(END_TEMPLATE);
        logger.info("Transformed template: " + sb.toString());
        return sb.toString();
    }
}
