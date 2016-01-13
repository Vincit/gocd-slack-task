package com.vincit.go.task.slack.curl;

public class SlackConfig {

    private final String webhookUrl;

    public SlackConfig(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

}
