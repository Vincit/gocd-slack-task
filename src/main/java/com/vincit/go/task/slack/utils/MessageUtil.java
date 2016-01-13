package com.vincit.go.task.slack.utils;

import java.util.Map;

public class MessageUtil {

    // Allows $, skips \$
    private static final String REGEX_PREFIX = "(?<!\\\\)\\$";
    private static final String ESCAPED_DOLLAR_REGEX = "\\\\\\$";

    public static String replaceWithEnvVars(String message, Map<String, String> envVars) {
        for (Map.Entry<String, String> entries : envVars.entrySet()) {
            message = message.replaceAll(REGEX_PREFIX + entries.getKey(), entries.getValue());
        }

        return message.replaceAll(ESCAPED_DOLLAR_REGEX, "\\$");
    }

}
