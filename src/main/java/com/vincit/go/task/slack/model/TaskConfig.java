package com.vincit.go.task.slack.model;

public class TaskConfig {

    private Config config;
    private Context context;

    public TaskConfig() {
    }

    public TaskConfig(Config config, Context context) {
        this.config = config;
        this.context = context;
    }

    public Config getConfig() {
        return config;
    }

    public Context getContext() {
        return context;
    }
}
