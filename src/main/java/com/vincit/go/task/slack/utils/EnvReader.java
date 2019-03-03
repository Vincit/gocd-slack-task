package com.vincit.go.task.slack.utils;

import in.ashwanthkumar.utils.lang.StringUtils;

public class EnvReader {
    
    public EnvReader() {}
    
    public String get(String name, String defaultValue) {
        final String value = System.getenv(name);
        if (StringUtils.isNotEmpty(value)) {
            return value;
        } else {
            return defaultValue;
        }
    }
    
    public String get(String name) {
        return get(name, null);
    }
}
