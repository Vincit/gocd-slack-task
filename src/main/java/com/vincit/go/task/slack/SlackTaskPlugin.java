/*************************GO-LICENSE-START*********************************
 * Copyright 2014 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *************************GO-LICENSE-END***********************************/

package com.vincit.go.task.slack;

import com.google.gson.GsonBuilder;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.*;
import com.vincit.go.task.slack.utils.JSONUtils;

import java.io.IOException;
import java.util.*;

import static com.vincit.go.task.slack.utils.FileUtils.getFileContents;
import static com.vincit.go.task.slack.utils.MessageUtil.replaceWithEnvVars;

@Extension
public class SlackTaskPlugin implements GoPlugin {

    public static final String CHANNEL = "Channel";
    public static final String MESSAGE = "Message";
    public static final String TITLE = "Title";
    public static final String ICON_OR_EMOJI = "IconOrEmoji";
    private Logger logger = Logger.getLoggerFor(SlackTaskPlugin.class);

    public static final String GET_PLUGIN_SETTINGS = "go.processor.plugin-settings.get";

    private GoApplicationAccessor goApplicationAccessor;

    @Override
    public void initializeGoApplicationAccessor(GoApplicationAccessor goApplicationAccessor) {
        this.goApplicationAccessor = goApplicationAccessor;
    }

    @Override
    public GoPluginApiResponse handle(GoPluginApiRequest request) throws UnhandledRequestTypeException {
        logger.info("Handle: " + request.requestName());

        if ("configuration".equals(request.requestName())) {
            return handleGetConfigRequest();
        } else if ("validate".equals(request.requestName())) {
            return handleValidation(request);
        } else if ("execute".equals(request.requestName())) {
            return handleTaskExecution(request);
        } else if ("view".equals(request.requestName())) {
            return handleTaskView();
        } else if ("go.plugin-settings.get-configuration".equals(request.requestName())) {
            return handlePluginConfig();
        } else if ("go.plugin-settings.get-view".equals(request.requestName())) {
            try {
                return handlePluginConfigView();
            } catch (IOException e) {
                String message = "Failed to find template: " + e.getMessage();
                return renderJSON(500, message);
            }
        } else if ("go.plugin-settings.validate-configuration".equals(request.requestName())) {
            return handleValidatePluginSettingsConfiguration(request);
        }
        throw new UnhandledRequestTypeException(request.requestName());
    }

    private GoPluginApiResponse handleValidatePluginSettingsConfiguration(GoPluginApiRequest goPluginApiRequest) {
        List<Map<String, Object>> response = new ArrayList<>();
        return renderJSON(200, response);
    }

    private GoPluginApiResponse renderJSON(final int responseCode, Object response) {
        final String json = response == null ? null : new GsonBuilder().create().toJson(response);
        return new GoPluginApiResponse() {
            @Override
            public int responseCode() {
                return responseCode;
            }

            @Override
            public Map<String, String> responseHeaders() {
                return null;
            }

            @Override
            public String responseBody() {
                return json;
            }
        };
    }

    private com.vincit.go.task.slack.SlackConfig getSlackConfigFromGo() {
        Map<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("plugin-id", "slack.task");
        GoApiResponse response = goApplicationAccessor.submit(com.vincit.go.task.slack.GoRequestFactory.createGoApiRequest(GET_PLUGIN_SETTINGS, JSONUtils.toJSON(requestMap)));

        Map<String, String> responseMap = response.responseBody() == null ?
                new HashMap<String, String>() :
                (Map<String, String>) JSONUtils.fromJSON(response.responseBody());

        return new com.vincit.go.task.slack.SlackConfig(responseMap.get("webhookUrl"));
    }

    private GoPluginApiResponse handlePluginConfigView() throws IOException {
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("displayValue", "Slack Task");
        response.put("template", getFileContents("/views/task.config.template.html"));
        return createResponse(200, response);
    }

    private GoPluginApiResponse handlePluginConfig() {
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("webhookUrl", createField("Webhook URL", 0, true));
        return renderJSON(200, response);
    }

    private GoPluginApiResponse handleTaskView() {
        int responseCode = DefaultGoApiResponse.SUCCESS_RESPONSE_CODE;
        HashMap view = new HashMap();
        view.put("displayValue", "Slack");
        try {
            view.put("template", getFileContents("/views/task.template.html"));
        } catch (Exception e) {
            responseCode = DefaultGoApiResponse.INTERNAL_ERROR;
            String errorMessage = "Failed to find template: " + e.getMessage();
            view.put("exception", errorMessage);
            logger.error(errorMessage, e);
        }
        return createResponse(responseCode, view);
    }

    private GoPluginApiResponse handleTaskExecution(GoPluginApiRequest request) {
        logger.info("handleTaskExecution");

        Map executionRequest = (Map) new GsonBuilder().create().fromJson(request.requestBody(), Object.class);
        Map config = (Map) executionRequest.get("config");
        Map context = (Map) executionRequest.get("context");

        com.vincit.go.task.slack.SlackConfig slackConfig = getSlackConfigFromGo();

        String webhookUrl = slackConfig.getWebhookUrl();
        com.vincit.go.task.slack.SlackExecutor executor = new com.vincit.go.task.slack.SlackExecutor(webhookUrl);

        String messageStr = replaceWithEnvVars(
                (String)((Map)config.get(MESSAGE)).get("value"),
                ((Map<String, String>)context.get("environmentVariables"))
        );

        SlackMessage message = new SlackMessage(
                (String)((Map)config.get(TITLE)).get("value"),
                messageStr,
                (String)((Map)config.get(ICON_OR_EMOJI)).get("value")
        );
        executor.sendMessage((String)((Map)config.get(CHANNEL)).get("value"), message);

        return createResponse(200, new HashMap());
    }

    private GoPluginApiResponse handleValidation(GoPluginApiRequest request) {
        HashMap validationResult = new HashMap();
        int responseCode = DefaultGoPluginApiResponse.SUCCESS_RESPONSE_CODE;
        return createResponse(responseCode, validationResult);
    }

    private GoPluginApiResponse handleGetConfigRequest() {
        HashMap config = new HashMap();
        config.put(CHANNEL, createField("Channel", 0, true));
        config.put(TITLE, createField("Title", 1, false));
        config.put(ICON_OR_EMOJI, createField("Icon or Emoji", 2, false));
        config.put(MESSAGE, createField("Message", 3, false));
        return createResponse(DefaultGoPluginApiResponse.SUCCESS_RESPONSE_CODE, config);
    }

    private HashMap createField(String name, int order, boolean required) {
        HashMap field = new HashMap();
        field.put("default-value", "");
        field.put("display-order", Integer.toString(order));
        field.put("display-name", name);
        field.put("required", required);
        return field;
    }

    private GoPluginApiResponse createResponse(int responseCode, Map body) {
        final DefaultGoPluginApiResponse response = new DefaultGoPluginApiResponse(responseCode);
        response.setResponseBody(new GsonBuilder().serializeNulls().create().toJson(body));
        return response;
    }

    @Override
    public GoPluginIdentifier pluginIdentifier() {
        return new GoPluginIdentifier("task", Arrays.asList("1.0"));
    }
}
