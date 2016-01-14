package com.vincit.go.task.slack;

import com.vincit.go.task.slack.model.ChannelType;
import in.ashwanthkumar.slack.webhook.Slack;
import in.ashwanthkumar.slack.webhook.SlackAttachment;

import java.io.IOException;

public class SlackExecutor {

    private final Slack slack;

    public SlackExecutor(String webhookUrl) {
        slack = new Slack(webhookUrl);
    }

    public void sendMessage(ChannelType channelType, String channel, TaskSlackMessage message) throws IOException {
        SlackAttachment attachment = new SlackAttachment(message.getMessage())
                .title(message.getTitle());

        updateChannel(channelType, channel);

        slack.icon(message.getIconOrEmoji());

        slack.push(attachment);
    }

    private void updateChannel(ChannelType channelType, String channel) {
        if (channelType == ChannelType.CHANNEL) {
            slack.sendToChannel(channel);
        } else {
            slack.sendToUser(channel);
        }
    }
}
