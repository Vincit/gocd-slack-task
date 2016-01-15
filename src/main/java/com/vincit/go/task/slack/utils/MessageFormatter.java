package com.vincit.go.task.slack.utils;

import java.util.Map;

public class MessageFormatter {

    // Allows $, skips \$
    private static final String REGEX_PREFIX = "(?<!\\\\)\\$";
    private static final String ESCAPED_DOLLAR_REGEX = "\\\\\\$";

    private Map<String, String> replacements;

    public MessageFormatter(Map<String, String> replacements) {
        this.replacements = replacements;
    }

    public String format(String message) {
        if (message == null) {
            return null;
        }

        for (Map.Entry<String, String> entries : replacements.entrySet()) {
            message = message.replaceAll(REGEX_PREFIX + entries.getKey(), entries.getValue());
        }

        return message.replaceAll(ESCAPED_DOLLAR_REGEX, "\\$");
    }

}
