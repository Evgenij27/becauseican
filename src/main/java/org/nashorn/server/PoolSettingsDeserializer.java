package org.nashorn.server;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class PoolSettingsDeserializer extends JsonDeserializer<PoolSettings> {

    @Override
    public PoolSettings deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);

        int corePoolSize = node.get("corePoolSize").asInt();
        if (corePoolSize < 0) {
            // thorw Execption
        }
        return null;
    }
}
