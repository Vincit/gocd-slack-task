package com.vincit.go.task.slack.executor;

import com.vincit.go.task.slack.model.MarkdownField;

import java.util.Set;

public class TaskSlackMessage {

    private final String title;
    private final String message;
    private final String iconOrEmoji;
    private final String color;
    private final String displayName;
    private final Set<MarkdownField> markdownIns;

    public TaskSlackMessage(String displayName, String title, String message, String iconOrEmoji, String color, Set<MarkdownField> markdownIns) {
        this.title = title;
        this.message = message;
        this.iconOrEmoji = iconOrEmoji;
        this.color = color;
        this.displayName = displayName;
        this.markdownIns = markdownIns;
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

    public Set<MarkdownField> getMarkdownIns() {
        return markdownIns;
    }
}
