package com.vincit.go.task.slack.utils;

import com.google.gson.*;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

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

    public static GoPluginApiResponse responseAsJson(int responseCode, Object body) {
        final DefaultGoPluginApiResponse response = new DefaultGoPluginApiResponse(responseCode);
        response.setResponseBody(new GsonBuilder().serializeNulls().create().toJson(body));
        return response;
    }
}
