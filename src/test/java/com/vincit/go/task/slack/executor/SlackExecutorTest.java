package com.vincit.go.task.slack.executor;

import com.vincit.go.task.slack.model.ChannelType;
import com.vincit.go.task.slack.model.MarkdownField;
import in.ashwanthkumar.slack.webhook.Slack;
import in.ashwanthkumar.slack.webhook.SlackAttachment;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.vincit.go.task.slack.utils.HashSets.asSet;
import static com.vincit.go.task.slack.utils.HashSets.valuesAsSet;
import static com.vincit.go.task.slack.utils.ReflectionUtil.getFieldValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SlackExecutorTest {

    @Test
    public void testSendMessageToChannel() throws Exception {
        Slack slack = mock(Slack.class);
        TaskSlackDestination destination = new TaskSlackDestination(
                "https://example.org/",
                ChannelType.CHANNEL,
                "channel-name"
        );

        SlackExecutor executor = new SlackExecutor(slack, destination);
        TaskSlackMessage message = new TaskSlackMessage(
                "displayName", "title", "message", "iconOrEmoji", "good",
                new HashSet<>(Arrays.asList(MarkdownField.TEXT, MarkdownField.PRETEXT))
        );
        executor.sendMessage(message);

        ArgumentCaptor<SlackAttachment> attachmentArgumentCaptor =
                ArgumentCaptor.forClass(SlackAttachment.class);

        verify(slack).sendToChannel("channel-name");
        verify(slack).push(attachmentArgumentCaptor.capture());
        verify(slack).displayName("displayName");
        verify(slack).icon("iconOrEmoji");

        SlackAttachment attachment = attachmentArgumentCaptor.getValue();
        Set<String> values = asSet(getFieldValue(attachment, "markdown", List.class));
        assertThat(values, is(valuesAsSet("pretext", "text")));

    }

    @Test
    public void testSendMessageToUser() throws Exception {
        Slack slack = mock(Slack.class);
        TaskSlackDestination destination = new TaskSlackDestination(
                "https://example.org/",
                ChannelType.USER,
                "channel-name"
        );

        SlackExecutor executor = new SlackExecutor(slack, destination);
        TaskSlackMessage message = new TaskSlackMessage(
                "displayName", "title", "message", "iconOrEmoji", "good",
                new HashSet<>(Arrays.asList(MarkdownField.TEXT, MarkdownField.PRETEXT))
        );
        executor.sendMessage(message);

        ArgumentCaptor<SlackAttachment> attachmentArgumentCaptor =
                ArgumentCaptor.forClass(SlackAttachment.class);

        verify(slack).sendToUser("channel-name");
        verify(slack).push(attachmentArgumentCaptor.capture());
        verify(slack).displayName("displayName");
        verify(slack).icon("iconOrEmoji");

        SlackAttachment attachment = attachmentArgumentCaptor.getValue();
        Set<String> values = asSet(getFieldValue(attachment, "markdown", List.class));
        assertThat(values, is(valuesAsSet("pretext", "text")));
    }


}