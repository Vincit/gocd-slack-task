package com.vincit.go.task.slack.executor;

public class TaskSlackMessage {

    private final String title;
    private final String message;
    private final String iconOrEmoji;
    private final String color;
    private final String displayName;

    public TaskSlackMessage(String displayName, String title, String message, String iconOrEmoji, String color) {
        this.title = title;
        this.message = message;
        this.iconOrEmoji = iconOrEmoji;
        this.color = color;
        this.displayName = displayName;
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

    public String getColor() {
        return color;
    }

    public String getDisplayName() {
        return displayName;
    }
}
