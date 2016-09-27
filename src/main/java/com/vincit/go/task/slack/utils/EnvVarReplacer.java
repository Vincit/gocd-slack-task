package com.vincit.go.task.slack.utils;

import java.util.HashMap;
import java.util.Map;

public class EnvVarReplacer {

    // Allows $, skips \$
    private static final String REGEX_PREFIX = "(?<!\\\\)\\$";
    private static final String ESCAPED_DOLLAR_REGEX = "\\\\\\$";

    private Map<String, String> replacements;

    public EnvVarReplacer() {
        replacements = new HashMap<>();
    }

    public EnvVarReplacer(Map<String, String> replacements) {
        this.replacements = replacements;
    }

    public String replace(String message) {
        if (message == null) {
            return null;
        }

        for (Map.Entry<String, String> entries : replacements.entrySet()) {
            message = message.replaceAll(REGEX_PREFIX + entries.getKey(), entries.getValue());
        }

        return message.replaceAll(ESCAPED_DOLLAR_REGEX, "\\$");
    }

    public void add(String key, String value) {
        this.replacements.put(key, value);
    }

}
