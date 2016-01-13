package com.vincit.go.task.slack.model;

import java.util.Map;

public class Context {

    private Map<String, String> environmentVariables;

    public Context() {
    }

    public Context(Map<String, String> environmentVariables) {
        this.environmentVariables = environmentVariables;
    }

    public Map<String, String> getEnvironmentVariables() {
        return environmentVariables;
    }
}
