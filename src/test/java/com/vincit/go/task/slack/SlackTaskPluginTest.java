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
import java.util.Map;

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

    @Test
    public void testHandleValidation() throws Exception {
        JsonUtil jsonUtil = mock(JsonUtil.class);
        when(jsonUtil.fromJSON("test-request", Config.class)).thenReturn(
                new Config(
                        prop("message"),
                        prop("title"),
                        prop("icon"),
                        prop("channel"),
                        requiredProp("Channel"),
                        requiredProp("webhook"),
                        prop("display-name"),
                        requiredProp("Custom"),
                        prop("00ff00")
                )
        );

        FileReader fileReader = mock(FileReader.class);
        SlackExecutor slackExecutor = mock(SlackExecutor.class);

        SlackTaskPlugin plugin = new SlackTaskPlugin(jsonUtil, fileReader, slackExecutor);

        DefaultGoPluginApiRequest request = new DefaultGoPluginApiRequest("task", "1.0", "validate");
        request.setRequestBody("test-request");
        plugin.handle(request);

        ArgumentCaptor<Object> objectCaptor = ArgumentCaptor.forClass(Object.class);
        verify(jsonUtil).responseAsJson(eq(200), objectCaptor.capture());

        Map<String, Map<String, String>> validationResult = (Map)objectCaptor.getValue();
        Map<String, Map<String, String>> expected = new HashMap<>();
        assertThat(validationResult, is(expected));
    }

    @Test
    public void testHandleValidationRequiredErrors() throws Exception {
        JsonUtil jsonUtil = mock(JsonUtil.class);
        when(jsonUtil.fromJSON("test-request", Config.class)).thenReturn(
                new Config(
                        prop("message"),
                        prop("title"),
                        prop("icon"),
                        prop("channel"),
                        requiredProp(""),
                        requiredProp(""),
                        prop("display-name"),
                        requiredProp(""),
                        prop("")
                )
        );

        FileReader fileReader = mock(FileReader.class);
        SlackExecutor slackExecutor = mock(SlackExecutor.class);

        SlackTaskPlugin plugin = new SlackTaskPlugin(jsonUtil, fileReader, slackExecutor);

        DefaultGoPluginApiRequest request = new DefaultGoPluginApiRequest("task", "1.0", "validate");
        request.setRequestBody("test-request");
        plugin.handle(request);

        ArgumentCaptor<Object> objectCaptor = ArgumentCaptor.forClass(Object.class);
        verify(jsonUtil).responseAsJson(eq(200), objectCaptor.capture());

        Map<String, Map<String, String>> validationResult = (Map)objectCaptor.getValue();
        Map<String, String> expected = new HashMap<>();
        expected.put(SlackTaskPlugin.CHANNEL_TYPE, "Channel type is required");
        expected.put(SlackTaskPlugin.WEBHOOK_URL, "Webhook URL is required");
        expected.put(SlackTaskPlugin.COLOR_TYPE, "Color type is required");
        assertThat(validationResult.get("errors"), is(expected));
    }

    private static Property prop(String value) {
        return new Property(value, false, false);
    }

    private static Property requiredProp(String value) {
        return new Property(value, false, true);
    }

}