package com.vincit.go.task.slack;

import com.thoughtworks.go.plugin.api.request.DefaultGoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoApiResponse;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.vincit.go.task.slack.executor.SlackExecutor;
import com.vincit.go.task.slack.executor.TaskSlackDestination;
import com.vincit.go.task.slack.executor.TaskSlackMessage;
import com.vincit.go.task.slack.model.*;
import com.vincit.go.task.slack.utils.FileReader;
import com.vincit.go.task.slack.utils.JsonUtil;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SlackTaskPluginTest {

    @Test
    public void testHandleTaskExecution() throws Exception {
        JsonUtil jsonUtil = mock(JsonUtil.class);
        when(jsonUtil.fromJSON("test-request", TaskConfig.class)).thenReturn(
                new TaskConfig(
                        new Config(
                                prop("message"),
                                prop("title"),
                                prop("icon"),
                                prop("channel"),
                                prop("CHANNEL"),
                                prop("webhook"),
                                prop("display-name"),
                                prop("CUSTOM"),
                                prop("00ff00")
                        ),
                        new Context(new HashMap<String, String>())
                )
        );
        when(jsonUtil.responseAsJson(eq(200), anyMap())).thenReturn(new DefaultGoPluginApiResponse(200));

        FileReader fileReader = mock(FileReader.class);
        SlackExecutor slackExecutor = mock(SlackExecutor.class);

        SlackTaskPlugin plugin = new SlackTaskPlugin(jsonUtil, fileReader, slackExecutor);

        DefaultGoPluginApiRequest request = new DefaultGoPluginApiRequest("task", "1.0", "execute");
        request.setRequestBody("test-request");
        GoPluginApiResponse response = plugin.handle(request);


        ArgumentCaptor<TaskSlackDestination> destinationCaptor = ArgumentCaptor.forClass(TaskSlackDestination.class);
        ArgumentCaptor<TaskSlackMessage> messageCaptor = ArgumentCaptor.forClass(TaskSlackMessage.class);
        verify(slackExecutor).sendMessage(destinationCaptor.capture(), messageCaptor.capture());


        assertThat(response.responseCode(), is(DefaultGoApiResponse.SUCCESS_RESPONSE_CODE));

        assertThat(destinationCaptor.getValue().getWebhookUrl(), is("webhook"));
        assertThat(destinationCaptor.getValue().getChannel(), is("channel"));
        assertThat(destinationCaptor.getValue().getChannelType(), is(ChannelType.CHANNEL));

        assertThat(messageCaptor.getValue().getDisplayName(), is("display-name"));
        assertThat(messageCaptor.getValue().getTitle(), is("title"));
        assertThat(messageCaptor.getValue().getMessage(), is("message"));
        assertThat(messageCaptor.getValue().getIconOrEmoji(), is("icon"));
        assertThat(messageCaptor.getValue().getColor(), is("00ff00"));

    }

    private static Property prop(String value) {
        return new Property(value);
    }

}