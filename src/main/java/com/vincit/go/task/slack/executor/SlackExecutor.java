package com.vincit.go.task.slack.executor;

import com.vincit.go.task.slack.model.ChannelType;
import com.vincit.go.task.slack.model.MarkdownField;
import in.ashwanthkumar.slack.webhook.Slack;
import in.ashwanthkumar.slack.webhook.SlackAttachment;

import java.io.IOException;

public class SlackExecutor {

    private Slack slack;
    private TaskSlackDestination destination;

    SlackExecutor(Slack slack, TaskSlackDestination destination) {
        this.slack = slack;
        this.destination = destination;
    }

    SlackExecutor(TaskSlackDestination destination) {
        this.destination = destination;
        this.slack = new Slack(destination.getWebhookUrl());
    }

    public void sendMessage(TaskSlackMessage message) throws IOException {
        SlackAttachment attachment = new SlackAttachment(message.getMessage())
                .color(message.getColor())
                .title(message.getTitle());

        for (MarkdownField field : message.getMarkdownIns()) {
            attachment.addMarkdownIn(field.getApiValue());
        }

        updateChannel(destination);

        slack.displayName(message.getDisplayName());
        slack.icon(message.getIconOrEmoji());

        slack.push(attachment);
    }

    private void updateChannel(TaskSlackDestination destination) {
        if (destination.getChannelType() == ChannelType.CHANNEL) {
            slack.sendToChannel(destination.getChannel());
        } else {
            slack.sendToUser(destination.getChannel());
        }
    }
}
