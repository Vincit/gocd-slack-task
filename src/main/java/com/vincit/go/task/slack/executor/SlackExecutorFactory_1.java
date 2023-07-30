package com.vincit.go.task.slack.executor;

public class SlackExecutorFactory_1 {

    public SlackExecutor forDestination(TaskSlackDestination destination) {
        return new SlackExecutor(destination);
    }

}
