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
                "            \"value\": \"Test message\"\n" +
                "        },\n" +
                "        \"Title\": {\n" +
                "            \"value\": \"Test title\"\n" +
                "        },\n" +
                "        \"IconOrEmoji\": {\n" +
                "            \"value\": \":tada:\"\n" +
                "        },\n" +
                "        \"Channel\": {\n" +
                "            \"value\": \"test-channel\"\n" +
                "        },\n" +
                "        \"ChannelType\": {\n" +
                "            \"value\": \"CHANNEL\"\n" +
                "        },\n" +
                "        \"WebhookUrl\": {\n" +
                "            \"value\": \"http://example.org\"\n" +
                "        },\n" +
                "        \"DisplayName\": {\n" +
                "            \"value\": \"Display Name\"\n" +
                "        },\n" +
                "        \"ColorType\": {\n" +
                "            \"value\": \"CUSTOM\"\n" +
                "        },\n" +
                "        \"Color\": {\n" +
                "            \"value\": \"F00F00\"\n" +
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
        assertThat(config.getConfig().getChannel(), is("test-channel"));
        assertThat(config.getConfig().getChannelType(), is(ChannelType.CHANNEL));
        assertThat(config.getConfig().getIconOrEmoji(), is(":tada:"));
        assertThat(config.getConfig().getDisplayName(), is("Display Name"));
        assertThat(config.getConfig().getColorType(), is(ColorType.CUSTOM));
        assertThat(config.getConfig().getColor(), is("F00F00"));

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
                "            \"value\": \"Test message\"\n" +
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
        assertThat(config.getConfig().getTitle(), is(""));
        assertThat(config.getConfig().getDisplayName(), is(""));
        assertThat(config.getConfig().getChannel(), nullValue());
        assertThat(config.getConfig().getIconOrEmoji(), nullValue());
        assertThat(config.getConfig().getCustomColor(), nullValue());
        assertThat(config.getConfig().getColorType(), nullValue());
        assertThat(config.getConfig().getWebhookUrl(), nullValue());

        assertThat(config.getContext(), notNullValue());
        assertThat(config.getContext().getEnvironmentVariables(), notNullValue());
        assertThat(config.getContext().getEnvironmentVariables().get("ENV1"), is("VAL1"));
    }
}