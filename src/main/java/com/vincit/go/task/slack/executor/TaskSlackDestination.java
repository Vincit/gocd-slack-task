package com.vincit.go.task.slack.executor;

import com.vincit.go.task.slack.model.ChannelType;

import java.util.Objects;

public class TaskSlackDestination {

    private String webhookUrl;
    private ChannelType channelType;
    private String channel;

    public TaskSlackDestination(String webhookUrl, ChannelType channelType, String channel) {
        Objects.requireNonNull(webhookUrl, "Webhook URL must not be null");
        Objects.requireNonNull(channelType, "Channel type must not be null");
        Objects.requireNonNull(channel, "Channel must not be null");

        this.webhookUrl = webhookUrl;

        if (channelType == ChannelType.TEXT) {

            String prefix = channel.substring(0, 1);
            if ("#".equals(prefix)) {
                this.channelType = ChannelType.CHANNEL;
            } else if ("@".equals(prefix)) {
                this.channelType = ChannelType.USER;
            } else {
                throw new IllegalStateException("Invalid channel prefix <" + prefix + ">");
            }
        } else {
            this.channelType = channelType;
        }

        if (channelType == ChannelType.TEXT) {
            if (channel.length() <= 1) {
                throw new IllegalStateException("Channel name too short. Must be at least 2 including the type prefix @ or #.");
            }
            this.channel = channel.substring(1);
        } else {
            if (channel.length() < 1) {
                throw new IllegalStateException("Channel name too short. Must be at least 1.");
            }
            this.channel = channel;
        }
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public ChannelType getChannelType() {
        return channelType;
    }

    public String getChannel() {
        return channel;
    }
}
