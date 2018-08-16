package com.vincit.go.task.slack.model;


import com.vincit.go.task.slack.utils.FileReader;
import com.vincit.go.task.slack.utils.JsonUtil;
import com.vincit.go.task.slack.utils.EnvVarReplacer;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

public class ConfigTest {

    public static final String COLOR_CODE_ERROR_MESSAGE = "Color must be given as three/six hexadecimals (without # prefix) or it must be an environment variable";

    @Test
    public void testCustomColor() {
        assertThat(configWithColor("Custom", "F00F00").getColor(), is("F00F00"));
    }

    @Test
    public void testNoColor() {
        assertThat(configWithColor("None", "F00F00").getColor(), nullValue());
        assertThat(configWithColor("None", null).getColor(), nullValue());
    }

    @Test
    public void testGoodColor() {
        assertThat(configWithColor("Good", "F00F00").getColor(), is("good"));
        assertThat(configWithColor("Good", null).getColor(), is("good"));
    }

    @Test
    public void testWarningColor() {
        assertThat(configWithColor("Warning", "F00F00").getColor(), is("warning"));
        assertThat(configWithColor("Warning", null).getColor(), is("warning"));
    }

    @Test
    public void testDangerColor() {
        assertThat(configWithColor("Danger", "F00F00").getColor(), is("danger"));
        assertThat(configWithColor("Danger", null).getColor(), is("danger"));
    }

    @Test
    public void testChannel() {
        Config config = configWithChannel("Channel", "test-channel");
        assertThat(config.getChannelType(), is(ChannelType.CHANNEL));
        assertThat(config.getChannel(), is("test-channel"));
    }

    @Test
    public void testNullChannel() {
        Config config = configWithChannel("Channel", null);
        assertThat(config.getChannelType(), is(ChannelType.CHANNEL));
        assertThat(config.getChannel(), nullValue());
    }

    @Test
    public void testEmptyChannel() {
        Config config = configWithChannel("Channel", "");
        assertThat(config.getChannelType(), is(ChannelType.CHANNEL));
        assertThat(config.getChannel(), nullValue());
    }

    @Test
    public void testNullUser() {
        Config config = configWithChannel("User", null);
        assertThat(config.getChannelType(), is(ChannelType.USER));
        assertThat(config.getChannel(), nullValue());
    }

    @Test
    public void testEmptyUser() {
        Config config = configWithChannel("User", "");
        assertThat(config.getChannelType(), is(ChannelType.USER));
        assertThat(config.getChannel(), nullValue());
    }

    @Test
    public void testUser() {
        Config config = configWithChannel("User", "test.user");
        assertThat(config.getChannelType(), is(ChannelType.USER));
        assertThat(config.getChannel(), is("test.user"));
    }

    @Test
    public void validateColorFormat_ColorCode() {
        Config config = configWithColor("Custom", "00ff00");
        Map<String, String> errors = config.validate();

        assertThat(errors.isEmpty(), is(true));
    }

    @Test
    public void validateColorFormat_ShortColorCode() {
        Config config = configWithColor("Custom", "f00");
        Map<String, String> errors = config.validate();

        assertThat(errors.isEmpty(), is(true));
    }

    @Test
    public void validateColorFormat_EnvVar() {
        Config config = configWithColor("Custom", "$ABCXYZabcxyz_1237980");
        Map<String, String> errors = config.validate();

        assertThat(errors.isEmpty(), is(true));
    }

    @Test
    public void validateInvalidColorFormat_EnvVar() {
        Config config = configWithColor("Custom", "$ABCXYZab cxyz_1237980");
        Map<String, String> errors = config.validate();

        assertThat(errors.isEmpty(), is(false));
    }

    @Test
    public void validateInvalidColorFormat() {
        Config config = configWithColor("Custom", "qwerty");
        Map<String, String> errors = config.validate();

        Map<String, String> expected = new HashMap<>();
        expected.put("Color", COLOR_CODE_ERROR_MESSAGE);
        assertThat(errors, is(expected));
    }

    @Test
    public void validateInvalidColorFormat_Hash() {
        Config config = configWithColor("Custom", "#00ff00");
        Map<String, String> errors = config.validate();

        Map<String, String> expected = new HashMap<>();
        expected.put("Color", COLOR_CODE_ERROR_MESSAGE);
        assertThat(errors, is(expected));
    }

    @Test
    public void validateInvalidColorFormat_InvalidLengthShort() {
        Config config = configWithColor("Custom", "00");
        Map<String, String> errors = config.validate();

        Map<String, String> expected = new HashMap<>();
        expected.put("Color", COLOR_CODE_ERROR_MESSAGE);
        assertThat(errors, is(expected));
    }

    @Test
    public void validateInvalidColorFormat_InvalidLengthMiddle() {
        Config config = configWithColor("Custom", "00ff");
        Map<String, String> errors = config.validate();

        Map<String, String> expected = new HashMap<>();
        expected.put("Color", COLOR_CODE_ERROR_MESSAGE);
        assertThat(errors, is(expected));
    }

    @Test
    public void validateInvalidColorFormat_InvalidLengthLong() {
        Config config = configWithColor("Custom", "00ff00f");
        Map<String, String> errors = config.validate();

        Map<String, String> expected = new HashMap<>();
        expected.put("Color", COLOR_CODE_ERROR_MESSAGE);
        assertThat(errors, is(expected));
    }

    @Test
    public void validateCustomColor_Missing() {
        Config config = configWithColor("Custom", "");
        Map<String, String> errors = config.validate();

        Map<String, String> expected = new HashMap<>();
        expected.put("Color", "Color must be defined when using custom color");
        assertThat(errors, is(expected));
    }

    @Test
    public void parseTargetConfig() throws Exception {
        TaskConfig taskConfig = new JsonUtil().fromJSON(
                new FileReader().getFileContents("/json/task_config.json"),
                TaskConfig.class
        );

        assertThat(taskConfig.getConfig(), notNullValue());
        assertThat(taskConfig.getConfig().getWebhookUrl(), is("https://example.org"));
        assertThat(taskConfig.getConfig().getChannel(), is("channel"));
        assertThat(taskConfig.getConfig().getChannelType(), is(ChannelType.CHANNEL));
        assertThat(taskConfig.getConfig().getDisplayName(), is("display-name"));
        assertThat(taskConfig.getConfig().getTitle(), is("title"));
        assertThat(taskConfig.getConfig().getMessage(), is("message"));
        assertThat(taskConfig.getConfig().getIconOrEmoji(), is(":tada:"));
        assertThat(taskConfig.getConfig().getColorType(), is(ColorType.CUSTOM));
        assertThat(taskConfig.getConfig().getColor(), is("f00f00"));

        assertThat(taskConfig.getContext(), notNullValue());
        assertThat(taskConfig.getContext().getEnvironmentVariables(), hasKey("VAR1"));
        assertThat(taskConfig.getContext().getEnvironmentVariables(), hasKey("VAR2"));

    }

    @Test
    public void testMarkdownIns_Text() {
        Config config = configWithTextMarkdownIn();
        assertThat(config.getMarkdownIns(), hasItem(MarkdownField.TEXT));
        assertThat(config.getMarkdownIns().size(), is(1));
    }

    @Test
    public void testReplace() throws IOException {
        TaskConfig taskConfig = new JsonUtil().fromJSON(
                new FileReader().getFileContents("/json/task_config_env_var.json"),
                TaskConfig.class
        );

        EnvVarReplacer replacer = new EnvVarReplacer(
                taskConfig.getContext().getEnvironmentVariables()
        );

        Config config = taskConfig.getConfig()
                .replace(replacer);

        assertThat(config.getWebhookUrl(), is("https://example.org/hook"));
        assertThat(config.getChannel(), is("Channel value"));
        assertThat(config.getChannelType(), is(ChannelType.CHANNEL));
        assertThat(config.getDisplayName(), is("Display value"));
        assertThat(config.getTitle(), is("Title value"));
        assertThat(config.getMessage(), is("Message value"));
        assertThat(config.getIconOrEmoji(), is("Emoji value"));
        assertThat(config.getColorType(), is(ColorType.CUSTOM));
        assertThat(config.getColor(), is("f00baa"));
    }

    private Config configWithColor(String colorType, String color) {
        return new Config(prop(null), prop(null), prop(null), prop(null), prop(null), prop(null), prop(null), prop(colorType), prop(color), prop(null), prop(null));
    }

    private Config configWithChannel(String channelType, String channel) {
        return new Config(prop(null), prop(channel), prop(channelType), prop(null), prop(null), prop(null), prop(null), prop(null), prop(null), prop(null), prop(null));
    }

    private Config configWithTextMarkdownIn() {
        return new Config(prop(null), prop(null), prop(null), prop(null), prop(null), prop(null), prop(null), prop(null), prop(null), prop("true"), prop(null));
    }

    private Property prop(String value) {
        return new Property(value, false, false);
    }

}