package com.vincit.go.task.slack.utils;

import in.ashwanthkumar.slack.webhook.SlackAttachment;

import java.lang.reflect.Field;

public class ReflectionUtil {

    public static <T> T getFieldValue(Object object, String fieldName, Class<T> castTo) throws NoSuchFieldException, IllegalAccessException {
        Field field = SlackAttachment.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return castTo.cast(field.get(object));
    }

}
