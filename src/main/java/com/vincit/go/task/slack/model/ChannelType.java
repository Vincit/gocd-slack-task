package com.vincit.go.task.slack.model;

public enum ChannelType implements UiValue {

    CHANNEL("Channel"),
    USER("User"),
    /**
     * Type decided via prefix @ and #
     */
    TEXT("Text");

    String displayValue;

    ChannelType(String displayValue) {
        this.displayValue = displayValue;
    }

    @Override
    public String getDisplayValue() {
        return displayValue;
    }
}
