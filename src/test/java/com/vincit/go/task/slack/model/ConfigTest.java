package com.vincit.go.task.slack.model;


import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class ConfigTest {

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
    public void validateColorFormat() {
        Config config = configWithColor("Custom", "00ff00");
        Map<String, String> errors = config.validate();

        assertThat(errors.isEmpty(), is(true));
    }

    @Test
    public void validateInvalidColorFormat() {
        Config config = configWithColor("Custom", "qwerty");
        Map<String, String> errors = config.validate();

        Map<String, String> expected = new HashMap<>();
        expected.put("Color", "Color must be given as six hexadecimals (without # prefix)");
        assertThat(errors, is(expected));
    }

    @Test
    public void validateInvalidColorFormat_Hash() {
        Config config = configWithColor("Custom", "#00ff00");
        Map<String, String> errors = config.validate();

        Map<String, String> expected = new HashMap<>();
        expected.put("Color", "Color must be given as six hexadecimals (without # prefix)");
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

    private Config configWithColor(String colorType, String color) {
        return new Config(prop(null), prop(null), prop(null), prop(null), prop(null), prop(null), prop(null), prop(colorType), prop(color));
    }

    private Config configWithChannel(String channelType, String channel) {
        return new Config(prop(null), prop(channel), prop(channelType), prop(null), prop(null), prop(null), prop(null), prop(null), prop(null));
    }

    private Property prop(String value) {
        return new Property(value, false, false);
    }

}