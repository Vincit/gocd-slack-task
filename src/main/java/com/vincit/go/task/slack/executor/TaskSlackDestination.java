package com.vincit.go.task.slack.executor;

import com.vincit.go.task.slack.model.ChannelType;

public class TaskSlackDestination {

    private String webhookUrl;
    private ChannelType channelType;
    private String channel;

    public TaskSlackDestination(String webhookUrl, ChannelType channelType, String channel) {
        this.webhookUrl = webhookUrl;
        this.channelType = channelType;
        this.channel = channel;
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
