package com.vincit.go.task.slack.model;

public enum MarkdownField {

    PRETEXT("pretext"),
    TEXT("text"),
    FIELDS("fields");

    private final String apiValue;

    MarkdownField(String apiValue) {
        this.apiValue = apiValue;
    }

    public String getApiValue() {
        return apiValue;
    }
}
