package com.vincit.go.task.slack.curl.utils;

import com.google.gson.*;

import java.util.HashMap;
import java.util.Map;

public class JSONUtils {
    public static Object fromJSON(String json) {
        return new GsonBuilder().create().fromJson(json, Object.class);
    }

    public static String toJSON(Object object) {
        return new GsonBuilder().create().toJson(object);
    }

    public static Map<String, String> jsonArrayToMap(JsonArray pipelineParams) {
        Map<String, String> map = new HashMap<String, String>();

        for (JsonElement pipelineParam : pipelineParams) {
            JsonObject keyValuePair = pipelineParam.getAsJsonObject();
            map.put(keyValuePair.get("name").getAsString(), keyValuePair.get("value").getAsString());
        }

        return map;
    }
}
