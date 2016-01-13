package com.vincit.go.task.slack.model;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

public class TaskConfigParserTest {

    @Test
    public void testFromJson() throws Exception {

        TaskConfig config = TaskConfigParser.fromJson("{\n" +
                "    \"config\": {\n" +
                "        \"Message\": {\n" +
                "            \"secure\": false,\n" +
                "            \"value\": \"Test message\",\n" +
                "            \"required\": true\n" +
                "        },\n" +
                "        \"Title\": {\n" +
                "            \"secure\": false,\n" +
                "            \"value\": \"Test title\",\n" +
                "            \"required\": false\n" +
                "        },\n" +
                "        \"Channel\": {\n" +
                "            \"secure\": false,\n" +
                "            \"value\": \"#channel\",\n" +
                "            \"required\": false\n" +
                "        },\n" +
                "        \"IconOrEmoji\": {\n" +
                "            \"secure\": false,\n" +
                "            \"value\": \":test_icon:\",\n" +
                "            \"required\": true\n" +
                "        }\n" +
                "    },\n" +
                "    \"context\": {\n" +
                "        \"environmentVariables\": {\n" +
                "            \"ENV1\": \"VAL1\",\n" +
                "            \"ENV2\": \"VAL2\"\n" +
                "        },\n" +
                "        \"workingDirectory\": \"working-dir\"\n" +
                "    }\n" +
                "}");

        assertThat(config, notNullValue());

        assertThat(config.getConfig(), notNullValue());
        assertThat(config.getConfig().getMessage(), is("Test message"));
        assertThat(config.getConfig().getTitle(), is("Test title"));
        assertThat(config.getConfig().getChannel(), is("#channel"));
        assertThat(config.getConfig().getIconOrEmoji(), is(":test_icon:"));

        assertThat(config.getContext(), notNullValue());
        assertThat(config.getContext().getEnvironmentVariables(), notNullValue());
        assertThat(config.getContext().getEnvironmentVariables().get("ENV1"), is("VAL1"));
        assertThat(config.getContext().getEnvironmentVariables().get("ENV2"), is("VAL2"));
    }

    @Test
    public void testFromJsonWithNulls() throws Exception {

        TaskConfig config = TaskConfigParser.fromJson("{\n" +
                "    \"config\": {\n" +
                "        \"Message\": {\n" +
                "            \"secure\": false,\n" +
                "            \"value\": \"Test message\",\n" +
                "            \"required\": true\n" +
                "        },\n" +
                "        \"Title\": {\n" +
                "            \"secure\": false,\n" +
                "            \"value\": null,\n" +
                "            \"required\": false\n" +
                "        }\n" +
                "    },\n" +
                "    \"context\": {\n" +
                "        \"environmentVariables\": {\n" +
                "            \"ENV1\": \"VAL1\",\n" +
                "            \"ENV2\": \"VAL2\"\n" +
                "        },\n" +
                "        \"workingDirectory\": \"working-dir\"\n" +
                "    }\n" +
                "}");

        assertThat(config, notNullValue());

        assertThat(config.getConfig(), notNullValue());
        assertThat(config.getConfig().getMessage(), is("Test message"));
        assertThat(config.getConfig().getTitle(), nullValue());
        assertThat(config.getConfig().getChannel(), nullValue());
        assertThat(config.getConfig().getIconOrEmoji(), nullValue());

        assertThat(config.getContext(), notNullValue());
        assertThat(config.getContext().getEnvironmentVariables(), notNullValue());
        assertThat(config.getContext().getEnvironmentVariables().get("ENV1"), is("VAL1"));
        assertThat(config.getContext().getEnvironmentVariables().get("ENV3"), nullValue());
    }
}