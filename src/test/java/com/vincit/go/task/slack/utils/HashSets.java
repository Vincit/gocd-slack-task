package com.vincit.go.task.slack.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HashSets {

    public static <T> Set<T> valuesAsSet(T... values) {
        return asSet(Arrays.asList(values));
    }

    public static <T> Set<T> asSet(List<T> values) {
        return new HashSet<>(values);
    }

}
