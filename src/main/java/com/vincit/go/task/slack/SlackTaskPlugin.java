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

import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoApiResponse;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.vincit.go.task.slack.config.ConfigProvider;
import com.vincit.go.task.slack.executor.SlackExecutorFactory;
import com.vincit.go.task.slack.executor.TaskSlackDestination;
import com.vincit.go.task.slack.executor.TaskSlackMessage;
import com.vincit.go.task.slack.model.Config;
import com.vincit.go.task.slack.model.Context;
import com.vincit.go.task.slack.model.TaskConfig;
import com.vincit.go.task.slack.utils.EnvReader;
import com.vincit.go.task.slack.utils.EnvVarReplacer;
import com.vincit.go.task.slack.utils.FileReader;
import com.vincit.go.task.slack.utils.JsonUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Extension
public class SlackTaskPlugin extends AbstractTaskPlugin {

    private static final String CONFIG_FILE_PATH = "gocd-slack-task-config.json";
    private static final String HOME_DIR = System.getProperty("user.home");
    private static final String CRUISE_SERVER_DIR_ENV_VAR_NAME = "CRUISE_SERVER_DIR";
    private static final String CONFIG_DIR_ENV_VAR_NAME = "GOCD_SLACK_TASK_CONFIG_DIR";
    private Logger logger = Logger.getLoggerFor(SlackTaskPlugin.class);

    private JsonUtil jsonUtil;
    private FileReader fileReader;
    private SlackExecutorFactory slackExecutorFactory;
    private Config defaultConfig;
    private EnvReader envReader;

    public SlackTaskPlugin() {
        this(
                new JsonUtil(),
                new FileReader(),
                new SlackExecutorFactory(),
                new EnvReader()
        );
    }

    public SlackTaskPlugin(JsonUtil jsonUtil, FileReader fileReader, SlackExecutorFactory slackExecutorFactory,
                           EnvReader envReader) {
        super("task", "1.0");
        this.jsonUtil = jsonUtil;
        this.fileReader = fileReader;
        this.slackExecutorFactory = slackExecutorFactory;
        this.envReader = envReader;

        final String configFileContent = this.fileReader.getFileContents(
                joinPath(this.envReader.get(CONFIG_DIR_ENV_VAR_NAME), CONFIG_FILE_PATH),
                joinPath(HOME_DIR, CONFIG_FILE_PATH),
                joinPath(this.envReader.get(CRUISE_SERVER_DIR_ENV_VAR_NAME), CONFIG_FILE_PATH)
        );
        defaultConfig = jsonUtil.fromJSON(configFileContent, Config.class);
    }
    
    private String joinPath(String base, String file) {
        if (base != null) {
            final String pathSeparator = System.getProperty("file.separator");
            if (base.endsWith(pathSeparator)) {
                return base + file;
            } else {
                return base + pathSeparator + file;
            }
        } else {
            return null;
        }
    }

    @Override
    protected GoPluginApiResponse handleTaskView(GoPluginApiRequest request) {
        int responseCode = DefaultGoApiResponse.SUCCESS_RESPONSE_CODE;
        HashMap<String, String> view = new HashMap<>();
        view.put("displayValue", "Slack");
        try {
            view.put("template", fileReader.getFileContents("/views/task.template.html"));
        } catch (Exception e) {
            responseCode = DefaultGoApiResponse.INTERNAL_ERROR;
            String errorMessage = "Failed to find template: " + e.getMessage();
            view.put("exception", errorMessage);
            logger.error(errorMessage, e);
        }
        return jsonUtil.responseAsJson(responseCode, view);
    }

    @Override
    protected GoPluginApiResponse handleTaskExecution(GoPluginApiRequest request) {
        TaskConfig executionRequest = jsonUtil.fromJSON(request.requestBody(), TaskConfig.class);
        Context context = executionRequest.getContext();

        EnvVarReplacer envVarReplacer = new EnvVarReplacer(context.getEnvironmentVariables());

        Config config = executionRequest.getConfig()
                .or(defaultConfig)
                .replace(envVarReplacer);

        String webhookUrl = config.getWebhookUrl();

        TaskSlackMessage message = new TaskSlackMessage(
                config.getDisplayName(),
                config.getTitle(),
                config.getMessage(),
                config.getIconOrEmoji(),
                config.getColor(),
                config.getMarkdownIns()
        );

        TaskSlackDestination destination = new TaskSlackDestination(
                webhookUrl,
                config.getChannelType(),
                config.getChannel()
        );

        try {

            slackExecutorFactory.forDestination(destination).sendMessage(message);

        } catch (IOException e) {
            if(config.getFailOnError().equals("true")) {
                throw new RuntimeException("Could not send message to slack: " + e.getMessage(), e);
            } else {
                logger.error("Could not send message to slack: " + e.getMessage());
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        return jsonUtil.responseAsJson(DefaultGoApiResponse.SUCCESS_RESPONSE_CODE, result);
    }

    @Override
    protected GoPluginApiResponse handleValidation(GoPluginApiRequest request) {
        Config config = jsonUtil.fromJSON(request.requestBody(), Config.class);

        Map<String, Map<String, String>> validationResult = new HashMap<>();
        Map<String, String> errors = config.validate();

        int responseCode;
        if (!errors.isEmpty()) {
            validationResult.put("errors", errors);
            responseCode = DefaultGoApiResponse.SUCCESS_RESPONSE_CODE;
        } else {
            responseCode = DefaultGoPluginApiResponse.SUCCESS_RESPONSE_CODE;
        }

        return jsonUtil.responseAsJson(responseCode, validationResult);
    }

    @Override
    protected GoPluginApiResponse handleGetConfigRequest(GoPluginApiRequest request) {
        return jsonUtil.responseAsJson(
                DefaultGoPluginApiResponse.SUCCESS_RESPONSE_CODE,
                ConfigProvider.getFieldConfig()
        );
    }

}
