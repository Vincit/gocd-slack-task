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
import com.vincit.go.task.slack.model.Config;
import com.vincit.go.task.slack.model.Context;
import com.vincit.go.task.slack.model.TaskConfig;
import com.vincit.go.task.slack.utils.JSONUtils;

import java.io.IOException;
import java.util.*;

import static com.vincit.go.task.slack.utils.FieldUtils.createField;
import static com.vincit.go.task.slack.utils.FileUtils.getFileContents;
import static com.vincit.go.task.slack.utils.JSONUtils.responseAsJson;
import static com.vincit.go.task.slack.utils.MessageUtil.replaceWithEnvVars;

@Extension
public class SlackTaskPlugin implements GoPlugin {

    public static final String CHANNEL = "Channel";
    public static final String CHANNEL_TYPE = "ChannelType";
    public static final String MESSAGE = "Message";
    public static final String TITLE = "Title";
    public static final String ICON_OR_EMOJI = "IconOrEmoji";
    public static final String EXTENSION_ID = "task";
    public static final String PLUGIN_ID = "slack.task";
    public static final String WEBHOOK_URL = "webhookUrl";

    public static final int SUCCESS_RESPONSE_CODE = 200;
    public static final int ERROR_RESPONSE_CODE = 500;

    private Logger logger = Logger.getLoggerFor(SlackTaskPlugin.class);

    public static final String GET_PLUGIN_SETTINGS = "go.processor.plugin-settings.get";

    private GoApplicationAccessor goApplicationAccessor;

    @Override
    public void initializeGoApplicationAccessor(GoApplicationAccessor goApplicationAccessor) {
        this.goApplicationAccessor = goApplicationAccessor;
    }

    @Override
    public GoPluginApiResponse handle(GoPluginApiRequest request) throws UnhandledRequestTypeException {
        logger.info("Handle received message of type: " + request.requestName());

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
                return responseAsJson(ERROR_RESPONSE_CODE, message);
            }
        } else if ("go.plugin-settings.validate-configuration".equals(request.requestName())) {
            return handleValidatePluginSettingsConfiguration(request);
        }
        throw new UnhandledRequestTypeException(request.requestName());
    }

    private GoPluginApiResponse handleValidatePluginSettingsConfiguration(GoPluginApiRequest goPluginApiRequest) {
        List<Map<String, Object>> response = new ArrayList<>();
        return responseAsJson(SUCCESS_RESPONSE_CODE, response);
    }

    private SlackConfig getSlackConfigFromGo() {
        Map<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("plugin-id", PLUGIN_ID);
        GoRequestFactory r = new GoRequestFactory(pluginIdentifier());
        GoApiResponse response = goApplicationAccessor.submit(
                r.createGoApiRequest(GET_PLUGIN_SETTINGS, JSONUtils.toJSON(requestMap))
        );

        Map<String, String> responseMap = response.responseBody() == null ?
                new HashMap<String, String>() :
                (Map<String, String>) JSONUtils.fromJSON(response.responseBody());

        return new SlackConfig(responseMap.get(WEBHOOK_URL));
    }

    private GoPluginApiResponse handlePluginConfigView() throws IOException {
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("displayValue", "Slack Task");
        response.put("template", getFileContents("/views/task.config.template.html"));
        return responseAsJson(SUCCESS_RESPONSE_CODE, response);
    }

    private GoPluginApiResponse handlePluginConfig() {
        Map<String, Object> response = new HashMap<String, Object>();
        response.put(WEBHOOK_URL, createField("Webhook URL", 0, true));
        return responseAsJson(SUCCESS_RESPONSE_CODE, response);
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
        return responseAsJson(responseCode, view);
    }

    private GoPluginApiResponse handleTaskExecution(GoPluginApiRequest request) {
        logger.info("handleTaskExecution");

        TaskConfig executionRequest = new GsonBuilder().create().fromJson(request.requestBody(), TaskConfig.class);
        Config config = executionRequest.getConfig();
        Context context = executionRequest.getContext();

        SlackConfig slackConfig = getSlackConfigFromGo();

        try {
            String webhookUrl = slackConfig.getWebhookUrl();
            SlackExecutor executor = new SlackExecutor(webhookUrl);

            String messageStr = replaceWithEnvVars(
                    config.getMessage(),
                    context.getEnvironmentVariables()
            );

            TaskSlackMessage message = new TaskSlackMessage(
                    config.getTitle(),
                    messageStr,
                    config.getIconOrEmoji()
            );

            executor.sendMessage(config.getChannelType(), config.getChannel(), message);
        } catch (IOException e) {
            throw new RuntimeException("Could not send message to slack", e);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        return responseAsJson(SUCCESS_RESPONSE_CODE, result);
    }

    private GoPluginApiResponse handleValidation(GoPluginApiRequest request) {
        HashMap validationResult = new HashMap();
        int responseCode = DefaultGoPluginApiResponse.SUCCESS_RESPONSE_CODE;
        return responseAsJson(responseCode, validationResult);
    }

    private GoPluginApiResponse handleGetConfigRequest() {
        HashMap config = new HashMap();
        config.put(CHANNEL, createField("Channel", 0, true));
        config.put(CHANNEL_TYPE, createField("Channel Type", 1, true));
        config.put(TITLE, createField("Title", 2, false));
        config.put(ICON_OR_EMOJI, createField("Icon or Emoji", 3, false));
        config.put(MESSAGE, createField("Message", 4, false));
        return responseAsJson(DefaultGoPluginApiResponse.SUCCESS_RESPONSE_CODE, config);
    }

    @Override
    public GoPluginIdentifier pluginIdentifier() {
        return new GoPluginIdentifier(EXTENSION_ID, Arrays.asList("1.0"));
    }
}
