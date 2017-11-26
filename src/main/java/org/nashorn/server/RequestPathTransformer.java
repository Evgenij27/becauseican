package org.nashorn.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class RequestPathTransformer implements PathTransformer {

    private final Logger logger = Logger.getLogger(RequestPathTransformer.class);

    private static final String TEMPLATE_VAR = ":(?<tvar>[a-z]*)";
    private static final String TEMPLATE_REPLACEMENT = "(?<%s>[^/]+)";
    private static final String END_TEMPLATE = "(/)?";

    @Override
    public String transform(String template) {
        Pattern pattern = Pattern.compile(TEMPLATE_VAR);
        Matcher matcher = pattern.matcher(template);
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
