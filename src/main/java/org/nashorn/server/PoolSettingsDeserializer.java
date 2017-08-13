package org.nashorn.server;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PoolSettingsDeserializer extends JsonDeserializer<PoolSettings> {

    private static final String CORE_POOL_SIZE    = "corePoolSize";

    private static final String MAXIMUM_POOL_SIZE = "maximumPoolSize";

    private static final String KEEP_ALIVE_TIME   = "keepAliveTime";

    private static final String TIME_UNIT         = "timeUnit";

    private static final String WORK_QUEUE_SIZE   = "workQueueSize";

    @Override
    public PoolSettings deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);

        int corePoolSize = node.get(CORE_POOL_SIZE).asInt();
        if (corePoolSize < 1) {
            throw new InvalidValueException(
                    String.format("%s cannot be less than 1\n", CORE_POOL_SIZE));
        }

        int maximumPoolSize = node.get(MAXIMUM_POOL_SIZE).asInt();
        if (maximumPoolSize < 1 || maximumPoolSize < corePoolSize) {
            throw new InvalidValueException(
                    formatErrorMessage(
                            "%s cannot be less than 1 or less than %s\n",
                            MAXIMUM_POOL_SIZE,
                            CORE_POOL_SIZE));
        }

        int keepAliveTime = node.get(KEEP_ALIVE_TIME).asInt();
        if (keepAliveTime < 0) {
            throw new InvalidValueException(formatErrorMessage(
                    "%s cannot be less than 0\n",
                    KEEP_ALIVE_TIME));
        }

        String timeUnit = node.get(TIME_UNIT).asText();
        TimeUnit tu = resolveTimeUnit(timeUnit);

        int workQueueSize = node.get(WORK_QUEUE_SIZE).asInt();
        if (workQueueSize < 1 || workQueueSize > 100) {
            throw new InvalidValueException(formatErrorMessage("%s has to be within range from 1 to 100"));
        }

        return new PoolSettings(corePoolSize, maximumPoolSize, keepAliveTime, tu, workQueueSize);
    }

    private TimeUnit resolveTimeUnit(String tunit) throws JsonProcessingException {
        TimeUnit timeUnit = null;
        switch (tunit) {
            case "m":
                timeUnit = TimeUnit.MINUTES;
                break;
            case "s":
                timeUnit = TimeUnit.SECONDS;
                break;
            case "ms":
                timeUnit = TimeUnit.MILLISECONDS;
                break;
            case "us":
                timeUnit = TimeUnit.MICROSECONDS;
                break;
            case "ns":
                timeUnit = TimeUnit.NANOSECONDS;
                break;
            default:
                throw new InvalidValueException(
                        formatErrorMessage("Unknow TimeUnit: %s", tunit));
        }
        return timeUnit;
    }

    private String formatErrorMessage(String message, String... parameters) {
        return String.format(message, parameters);
    }
}
