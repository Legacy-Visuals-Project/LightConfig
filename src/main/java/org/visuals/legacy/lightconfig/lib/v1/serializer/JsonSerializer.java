package org.visuals.legacy.lightconfig.lib.v1.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.Strictness;

public class JsonSerializer extends ConfigSerializer<JsonElement> {
    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().setStrictness(Strictness.LENIENT).create();

    @Override
    public JsonElement deserialize(Object value) {
        if (!(value instanceof String string)) {
            return null;
        } else {
            return GSON.fromJson(string, JsonElement.class);
        }
    }

    @Override
    public byte[] serialize(JsonElement value) {
        if (value == null) {
            throw new RuntimeException("Cannot serialize null!");
        } else {
            return GSON.toJson(value).getBytes();
        }
    }
}
