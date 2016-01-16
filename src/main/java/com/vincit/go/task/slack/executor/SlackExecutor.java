package com.vincit.go.task.slack.executor;

import com.vincit.go.task.slack.model.ChannelType;
import in.ashwanthkumar.slack.webhook.Slack;
import in.ashwanthkumar.slack.webhook.SlackAttachment;

import java.io.IOException;

public class SlackExecutor {

    public void sendMessage(TaskSlackDestination destination, TaskSlackMessage message) throws IOException {
        Slack slack = new Slack(destination.getWebhookUrl());

        SlackAttachment attachment = new SlackAttachment(message.getMessage())
                .color(message.getColor())
                .title(message.getTitle());

        updateChannel(slack, destination);

        slack.displayName(message.getDisplayName());
        slack.icon(message.getIconOrEmoji());

        slack.push(attachment);
    }

    private void updateChannel(Slack slack, TaskSlackDestination destination) {
        if (destination.getChannelType() == ChannelType.CHANNEL) {
            slack.sendToChannel(destination.getChannel());
        } else {
            slack.sendToUser(destination.getChannel());
        }
    }
}
