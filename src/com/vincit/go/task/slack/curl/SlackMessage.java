package com.vincit.go.task.slack.curl;

public class SlackMessage {

    private final String title;
    private final String message;
    private final String iconOrEmoji;

    public SlackMessage(String title, String message, String iconOrEmoji) {
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
