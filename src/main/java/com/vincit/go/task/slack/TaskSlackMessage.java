package com.vincit.go.task.slack;

public class TaskSlackMessage {

    private final String title;
    private final String message;
    private final String iconOrEmoji;

    public TaskSlackMessage(String title, String message, String iconOrEmoji) {
        this.title = title;
        this.message = message;
        this.iconOrEmoji = iconOrEmoji;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getIconOrEmoji() {
        return iconOrEmoji;
    }
}
