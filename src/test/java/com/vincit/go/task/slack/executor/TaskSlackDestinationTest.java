package com.vincit.go.task.slack.executor;

import com.vincit.go.task.slack.model.ChannelType;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TaskSlackDestinationTest {

    private static final String WEBHOOK_URL = "http://example.org";
    private static final String TEXT_CHANNEL = "#channel";
    private static final String TEXT_USER = "@user.name";
    private static final String CHANNEL = "user.name";
    private static final String TEXT_INVALID_CHANNEL = "$user.name";

    @Test
    public void text_channel() throws Exception {
        TaskSlackDestination destination = new TaskSlackDestination(
                WEBHOOK_URL, ChannelType.TEXT, TEXT_CHANNEL
        );

        assertThat(destination.getChannel(), is("channel"));
        assertThat(destination.getChannelType(), is(ChannelType.CHANNEL));
    }

    @Test
    public void text_user() throws Exception {
        TaskSlackDestination destination = new TaskSlackDestination(
                WEBHOOK_URL, ChannelType.TEXT, TEXT_USER
        );

        assertThat(destination.getChannel(), is("user.name"));
        assertThat(destination.getChannelType(), is(ChannelType.USER));
    }

    @Test(expected = IllegalStateException.class)
    public void text_invalid() throws Exception {
        new TaskSlackDestination(
                WEBHOOK_URL, ChannelType.TEXT, TEXT_INVALID_CHANNEL
        );
    }

    @Test(expected = NullPointerException.class)
    public void text_null() throws Exception {
        new TaskSlackDestination(
                WEBHOOK_URL, ChannelType.TEXT, null
        );
    }

    @Test
    public void user() throws Exception {
        TaskSlackDestination destination = new TaskSlackDestination(
                WEBHOOK_URL, ChannelType.USER, "user.name"
        );

        assertThat(destination.getChannel(), is("user.name"));
        assertThat(destination.getChannelType(), is(ChannelType.USER));
    }

    @Test
    public void channel() throws Exception {
        TaskSlackDestination destination = new TaskSlackDestination(
                WEBHOOK_URL, ChannelType.CHANNEL, "channel"
        );

        assertThat(destination.getChannel(), is("channel"));
        assertThat(destination.getChannelType(), is(ChannelType.CHANNEL));
    }

    @Test(expected = NullPointerException.class)
    public void channel_null() throws Exception {
        new TaskSlackDestination(
                WEBHOOK_URL, ChannelType.CHANNEL, null
        );
    }

    @Test(expected = NullPointerException.class)
    public void user_null() throws Exception {
        new TaskSlackDestination(
                WEBHOOK_URL, ChannelType.USER, null
        );
    }

    @Test(expected = NullPointerException.class)
    public void channelType_null() throws Exception {
        new TaskSlackDestination(
                WEBHOOK_URL, null, "channel"
        );
    }

    @Test(expected = IllegalStateException.class)
    public void channel_tooShort() throws Exception {
        new TaskSlackDestination(
                WEBHOOK_URL, ChannelType.CHANNEL, ""
        );
    }

    @Test(expected = IllegalStateException.class)
    public void user_tooShort() throws Exception {
        new TaskSlackDestination(
                WEBHOOK_URL, ChannelType.USER, ""
        );
    }

    @Test(expected = IllegalStateException.class)
    public void text_tooShort() throws Exception {
        new TaskSlackDestination(
                WEBHOOK_URL, ChannelType.TEXT, "@"
        );
    }

    @Test
    public void webhookUrl() throws Exception {
        TaskSlackDestination destination = new TaskSlackDestination(
                WEBHOOK_URL, ChannelType.CHANNEL, "channel"
        );

        assertThat(destination.getWebhookUrl(), is(WEBHOOK_URL));
    }

    @Test(expected = NullPointerException.class)
    public void webhookUrl_null() throws Exception {
        new TaskSlackDestination(
                null, ChannelType.CHANNEL, "channel"
        );
    }

}