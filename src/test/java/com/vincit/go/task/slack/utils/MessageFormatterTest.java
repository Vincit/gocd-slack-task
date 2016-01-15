package com.vincit.go.task.slack.utils;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class MessageFormatterTest {

    @Test
    public void testReplace() {
        Map<String, String> envVars = new HashMap<>();
        envVars.put("WHAT", "World");
        assertThat(new MessageFormatter(envVars).format("Hello $WHAT"), is("Hello World"));
    }

    @Test
    public void testReplaceNull() {
        Map<String, String> envVars = new HashMap<>();
        envVars.put("WHAT", "World");
        assertThat(new MessageFormatter(envVars).format(null), nullValue());
    }

    @Test
    public void testReplaceMany() {
        Map<String, String> envVars = new HashMap<>();
        envVars.put("VAR_1", "Jee");
        envVars.put("VAR_2", "Joo");
        envVars.put("VAR_3", "Jaa");
        assertThat(new MessageFormatter(envVars).format("$VAR_1 $VAR_2 $VAR_3"), is("Jee Joo Jaa"));
    }

    @Test
    public void testReplaceOnlySingleDollar() {
        Map<String, String> envVars = new HashMap<>();
        envVars.put("VAR_1", "Jee");
        envVars.put("VAR_2", "Joo");
        envVars.put("VAR_3", "Jaa");
        assertThat(new MessageFormatter(envVars).format("$VAR_1 $$VAR_2 $VAR_3"), is("Jee $Joo Jaa"));
    }

    @Test
    public void testDontReplaceWithoutDollar() {
        Map<String, String> envVars = new HashMap<>();
        envVars.put("VAR_1", "Jee");
        envVars.put("VAR_2", "Joo");
        envVars.put("VAR_3", "Jaa");
        assertThat(new MessageFormatter(envVars).format("$VAR_1 VAR_2 $VAR_3"), is("Jee VAR_2 Jaa"));
    }

    @Test
    public void testDontReplaceEscaped() {
        Map<String, String> envVars = new HashMap<>();
        envVars.put("VAR_1", "Jee");
        envVars.put("VAR_2", "Joo");
        envVars.put("VAR_3", "Jaa");
        assertThat(new MessageFormatter(envVars).format("$VAR_1 \\$VAR_2 $VAR_3"), is("Jee $VAR_2 Jaa"));
    }

}