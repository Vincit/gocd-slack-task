package com.vincit.go.task.slack.model;

import com.vincit.go.task.slack.utils.EnvVarReplacer;

import java.util.Map;

public class Property {

    private String value;
    private boolean secure;
    private boolean required;

    public Property() {
    }

    public Property(String value, boolean secure, boolean required) {
        this.value = value;
        this.secure = secure;
        this.required = required;
    }

    public String getValue() {
        if (!isValid()) {
            throw new RuntimeException("value is required");
        }

        return value;
    }

    public String getValueOr(String other) {
        if (value != null) {
            if (value.isEmpty()) {
                return other;
            } else {
                return value;
            }
        } else {
            return other;
        }
    }

    public boolean isSecure() {
        return secure;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isPresent() {
        return value != null && !value.isEmpty();
    }

    public boolean isValid() {
        return !required || isPresent();
    }

    public void validate(String fieldName, String messageIfNotPreset, Map<String, String> errors) {
        if (!isValid()) {
            errors.put(fieldName, messageIfNotPreset);
        }
    }

    public Property replace(EnvVarReplacer envVarReplacer) {
        return new Property(
                envVarReplacer.replace(this.value),
                this.secure,
                this.required
        );
    }

    public Property or(Property defaultValue) {
        if (isPresent()) {
            return this;
        } else {
            return defaultValue;
        }
    }
}
