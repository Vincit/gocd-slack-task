package com.vincit.go.task.slack;

import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.request.DefaultGoApiRequest;
import com.thoughtworks.go.plugin.api.request.GoApiRequest;

public class GoRequestFactory {

    GoPluginIdentifier pluginIdentifier;

    public GoRequestFactory(GoPluginIdentifier pluginIdentifier) {
        this.pluginIdentifier = pluginIdentifier;
    }

    public GoApiRequest createGoApiRequest(final String api, final String responseBody) {
        DefaultGoApiRequest apiRequest = new DefaultGoApiRequest(api, "1.0", pluginIdentifier);
        apiRequest.setRequestBody(responseBody);
        return apiRequest;
    }

}
