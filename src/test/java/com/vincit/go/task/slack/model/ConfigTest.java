package com.vincit.go.task.slack.model;


import org.junit.Test;

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

    private Config configWithColor(String colorType, String color) {
        return new Config(null, null, null, null, null, null, null, prop(colorType), prop(color));
    }

    private Config configWithChannel(String channelType, String channel) {
        return new Config(null, null, null, prop(channel), prop(channelType), null, null, null, null);
    }

    private Property prop(String value) {
        return new Property(value);
    }

}