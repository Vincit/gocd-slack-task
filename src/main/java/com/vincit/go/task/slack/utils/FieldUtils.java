package com.vincit.go.task.slack.utils;

import java.util.HashMap;

public class FieldUtils {

    public static HashMap<String, Object> createField(String name, String defaultValue, int order, boolean required) {
        HashMap<String, Object> field = new HashMap<>();
        field.put("default-value", defaultValue);
        field.put("display-order", Integer.toString(order));
        field.put("display-name", name);
        field.put("required", required);
        return field;
    }

}
