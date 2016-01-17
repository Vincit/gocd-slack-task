package com.vincit.go.task.slack.config;

import com.vincit.go.task.slack.utils.Required;
import com.vincit.go.task.slack.utils.Secure;

import java.util.HashMap;

public class FieldUtils {

    public static HashMap<String, Object> createField(String defaultValue, Secure secure, Required required) {
        HashMap<String, Object> field = new HashMap<>();
        field.put("default-value", defaultValue);
        field.put("secure", secure == Secure.YES);
        field.put("required", required == Required.YES);
        return field;
    }

}
