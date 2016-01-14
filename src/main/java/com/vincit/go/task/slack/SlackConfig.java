package com.vincit.go.task.slack;

public class SlackConfig {

    private final String webhookUrl;

    public SlackConfig(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SlackConfig{");
        sb.append("webhookUrl='").append(webhookUrl).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
