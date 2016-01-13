package com.vincit.go.task.slack.model;

import com.google.gson.Gson;

public class TaskConfigParser {

    public static TaskConfig fromJson(String json) {
        return new Gson().fromJson(json, TaskConfig.class);
    }

}
