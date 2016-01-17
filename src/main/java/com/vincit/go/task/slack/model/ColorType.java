package com.vincit.go.task.slack.model;

public enum ColorType implements UiValue {

    NONE("None"),
    GOOD("Good"),
    WARNING("Warning"),
    DANGER("Danger"),
    CUSTOM("Custom");

    String displayValue;

    ColorType(String displayValue) {
        this.displayValue = displayValue;
    }

    @Override
    public String getDisplayValue() {
        return displayValue;
    }
}
