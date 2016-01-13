package com.vincit.go.task.slack.utils;

import java.util.HashMap;

public class FieldUtils {

    public static HashMap createField(String name, int order, boolean required) {
        HashMap field = new HashMap();
        field.put("default-value", "");
        field.put("display-order", Integer.toString(order));
        field.put("display-name", name);
        field.put("required", required);
        return field;
    }

}
