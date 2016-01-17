package com.vincit.go.task.slack.model;

public enum ColorType {

    NONE("None"),
    GOOD("Good"),
    WARNING("Warning"),
    DANGER("Danger"),
    CUSTOM("Custom");

    String displayValue;

    ColorType(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
