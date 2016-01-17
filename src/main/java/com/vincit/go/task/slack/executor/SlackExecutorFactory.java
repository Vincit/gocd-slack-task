package com.vincit.go.task.slack.executor;

public class SlackExecutorFactory {

    public SlackExecutor forDestination(TaskSlackDestination destination) {
        return new SlackExecutor(destination);
    }

}
