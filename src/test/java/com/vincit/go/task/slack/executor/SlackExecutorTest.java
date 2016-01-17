package com.vincit.go.task.slack.executor;

import com.vincit.go.task.slack.model.ChannelType;
import in.ashwanthkumar.slack.webhook.Slack;
import in.ashwanthkumar.slack.webhook.SlackAttachment;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SlackExecutorTest {

    @Test
    public void testSendMessageToChannel() throws IOException {
        Slack slack = mock(Slack.class);
        TaskSlackDestination destination = new TaskSlackDestination(
                "https://example.org/",
                ChannelType.CHANNEL,
                "channel-name"
        );

        SlackExecutor executor = new SlackExecutor(slack, destination);
        TaskSlackMessage message = new TaskSlackMessage(
                "displayName", "title", "message", "iconOrEmoji", "good"
        );
        executor.sendMessage(message);

        verify(slack).sendToChannel("channel-name");
        verify(slack).push(any(SlackAttachment.class));
        verify(slack).displayName("displayName");
        verify(slack).icon("iconOrEmoji");
    }

    @Test
    public void testSendMessageToUser() throws IOException {
        Slack slack = mock(Slack.class);
        TaskSlackDestination destination = new TaskSlackDestination(
                "https://example.org/",
                ChannelType.USER,
                "channel-name"
        );

        SlackExecutor executor = new SlackExecutor(slack, destination);
        TaskSlackMessage message = new TaskSlackMessage(
                "displayName", "title", "message", "iconOrEmoji", "good"
        );
        executor.sendMessage(message);

        verify(slack).sendToUser("channel-name");
        verify(slack).push(any(SlackAttachment.class));
        verify(slack).displayName("displayName");
        verify(slack).icon("iconOrEmoji");
    }

}