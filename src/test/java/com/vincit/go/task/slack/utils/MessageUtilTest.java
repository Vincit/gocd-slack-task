package com.vincit.go.task.slack.utils;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class MessageUtilTest {

    @Test
    public void testReplace() {
        Map<String, String> envVars = new HashMap<>();
        envVars.put("WHAT", "World");
        assertThat(MessageUtil.replaceWithEnvVars("Hello $WHAT", envVars), is("Hello World"));
    }

    @Test
    public void testReplaceMany() {
        Map<String, String> envVars = new HashMap<>();
        envVars.put("VAR_1", "Jee");
        envVars.put("VAR_2", "Joo");
        envVars.put("VAR_3", "Jaa");
        assertThat(MessageUtil.replaceWithEnvVars("$VAR_1 $VAR_2 $VAR_3", envVars), is("Jee Joo Jaa"));
    }

    @Test
    public void testReplaceOnlySingleDollar() {
        Map<String, String> envVars = new HashMap<>();
        envVars.put("VAR_1", "Jee");
        envVars.put("VAR_2", "Joo");
        envVars.put("VAR_3", "Jaa");
        assertThat(MessageUtil.replaceWithEnvVars("$VAR_1 $$VAR_2 $VAR_3", envVars), is("Jee $Joo Jaa"));
    }

    @Test
    public void testDontReplaceWithoutDollar() {
        Map<String, String> envVars = new HashMap<>();
        envVars.put("VAR_1", "Jee");
        envVars.put("VAR_2", "Joo");
        envVars.put("VAR_3", "Jaa");
        assertThat(MessageUtil.replaceWithEnvVars("$VAR_1 VAR_2 $VAR_3", envVars), is("Jee VAR_2 Jaa"));
    }

    @Test
    public void testDontReplaceEscaped() {
        Map<String, String> envVars = new HashMap<>();
        envVars.put("VAR_1", "Jee");
        envVars.put("VAR_2", "Joo");
        envVars.put("VAR_3", "Jaa");
        assertThat(MessageUtil.replaceWithEnvVars("$VAR_1 \\$VAR_2 $VAR_3", envVars), is("Jee $VAR_2 Jaa"));
    }

}