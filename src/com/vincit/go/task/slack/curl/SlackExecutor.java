package com.vincit.go.task.slack.curl;

import in.ashwanthkumar.slack.webhook.Slack;

public class SlackExecutor {

    private final Slack slack;

    public SlackExecutor(String webhookUrl) {
        slack = new Slack(webhookUrl);
    }

    public void sendMessage(String channel, SlackMessage message) {
        // TODO: Send message
    }
}
