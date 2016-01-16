package com.vincit.go.task.slack.utils;

import com.google.gson.GsonBuilder;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

public class JsonUtil {

    public <T> T fromJSON(String json, Class<T> targetType) {
        return new GsonBuilder().create().fromJson(json, targetType);
    }

    public GoPluginApiResponse responseAsJson(int responseCode, Object body) {
        final DefaultGoPluginApiResponse response = new DefaultGoPluginApiResponse(responseCode);
        response.setResponseBody(new GsonBuilder().serializeNulls().create().toJson(body));
        return response;
    }
}
