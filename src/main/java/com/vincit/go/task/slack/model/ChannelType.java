package com.vincit.go.task.slack.model;

public enum ChannelType {

    CHANNEL("Channel"),
    USER("User");

    String displayValue;

    ChannelType(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
