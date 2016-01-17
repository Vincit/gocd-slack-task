package com.vincit.go.task.slack;

import com.thoughtworks.go.plugin.api.request.DefaultGoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoApiResponse;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.vincit.go.task.slack.config.ConfigProvider;
import com.vincit.go.task.slack.executor.*;
import com.vincit.go.task.slack.model.*;
import com.vincit.go.task.slack.utils.FileReader;
import com.vincit.go.task.slack.utils.JsonUtil;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SlackTaskPluginTest {

    private static class MockSlackContainer {

        private SlackExecutor slackExecutor;
        private SlackExecutorFactory slackExecutorFactory;
        private ArgumentCaptor<TaskSlackDestination> destinationCaptor;

        public MockSlackContainer(SlackExecutor slackExecutor, SlackExecutorFactory slackExecutorFactory, ArgumentCaptor<TaskSlackDestination> destinationCaptor) {
            this.slackExecutor = slackExecutor;
            this.slackExecutorFactory = slackExecutorFactory;
            this.destinationCaptor = destinationCaptor;
        }
    }

    private MockSlackContainer mockSlack() {
        SlackExecutorFactory slackExecutorFactory = mock(SlackExecutorFactory.class);
        SlackExecutor slackExecutor = mock(SlackExecutor.class);
        ArgumentCaptor<TaskSlackDestination> destinationCaptor = ArgumentCaptor.forClass(TaskSlackDestination.class);
        when(slackExecutorFactory.forDestination(destinationCaptor.capture())).thenReturn(slackExecutor);

        return new MockSlackContainer(
                slackExecutor,
                slackExecutorFactory,
                destinationCaptor
        );
    }


    @Test
    public void testHandleTaskExecution() throws Exception {
        JsonUtil jsonUtil = mock(JsonUtil.class);
        when(jsonUtil.fromJSON("test-request", TaskConfig.class)).thenReturn(
                new TaskConfig(
                        new Config(
                                prop("webhook"), prop("channel"), prop("CHANNEL"), prop("display-name"), prop("title"), prop("message"),
                                prop("icon"),
                                prop("CUSTOM"),
                                prop("00ff00")
                        ),
                        new Context(new HashMap<String, String>())
                )
        );
        when(jsonUtil.responseAsJson(eq(200), anyMap())).thenReturn(new DefaultGoPluginApiResponse(200));

        FileReader fileReader = mock(FileReader.class);
        MockSlackContainer slack = mockSlack();

        SlackTaskPlugin plugin = new SlackTaskPlugin(jsonUtil, fileReader, slack.slackExecutorFactory);

        DefaultGoPluginApiRequest request = new DefaultGoPluginApiRequest("task", "1.0", "execute");
        request.setRequestBody("test-request");
        GoPluginApiResponse response = plugin.handle(request);


        ArgumentCaptor<TaskSlackMessage> messageCaptor = ArgumentCaptor.forClass(TaskSlackMessage.class);
        verify(slack.slackExecutor).sendMessage(messageCaptor.capture());


        assertThat(response.responseCode(), is(DefaultGoApiResponse.SUCCESS_RESPONSE_CODE));

        assertThat(slack.destinationCaptor.getValue().getWebhookUrl(), is("webhook"));
        assertThat(slack.destinationCaptor.getValue().getChannel(), is("channel"));
        assertThat(slack.destinationCaptor.getValue().getChannelType(), is(ChannelType.CHANNEL));

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
                        requiredProp("webhook"), prop("channel"), requiredProp("Channel"), prop("display-name"), prop("title"), prop("message"),
                        prop("icon"),
                        requiredProp("Custom"),
                        prop("00ff00")
                )
        );
        when(jsonUtil.responseAsJson(eq(200), any(Object.class))).thenReturn(new DefaultGoPluginApiResponse(200));

        FileReader fileReader = mock(FileReader.class);
        SlackExecutor slackExecutor = mock(SlackExecutor.class);
        MockSlackContainer slack = mockSlack();

        SlackTaskPlugin plugin = new SlackTaskPlugin(jsonUtil, fileReader, slack.slackExecutorFactory);

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
                        requiredProp(""), prop("channel"), requiredProp(""), prop("display-name"), prop("title"), prop("message"),
                        prop("icon"),
                        requiredProp(""),
                        prop("")
                )
        );
        when(jsonUtil.responseAsJson(eq(200), any())).thenReturn(new DefaultGoPluginApiResponse(200));

        FileReader fileReader = mock(FileReader.class);
        SlackExecutor slackExecutor = mock(SlackExecutor.class);
        MockSlackContainer slack = mockSlack();

        SlackTaskPlugin plugin = new SlackTaskPlugin(jsonUtil, fileReader, slack.slackExecutorFactory);

        DefaultGoPluginApiRequest request = new DefaultGoPluginApiRequest("task", "1.0", "validate");
        request.setRequestBody("test-request");
        plugin.handle(request);

        ArgumentCaptor<Object> objectCaptor = ArgumentCaptor.forClass(Object.class);
        verify(jsonUtil).responseAsJson(eq(200), objectCaptor.capture());

        Map<String, Map<String, String>> validationResult = (Map)objectCaptor.getValue();
        Map<String, String> expected = new HashMap<>();
        expected.put(ConfigProvider.CHANNEL_TYPE, "Channel type is required");
        expected.put(ConfigProvider.WEBHOOK_URL, "Webhook URL is required");
        expected.put(ConfigProvider.COLOR_TYPE, "Color type is required");
        assertThat(validationResult.get("errors"), is(expected));
    }

    @Test
    public void testConfig() throws Exception {
        JsonUtil jsonUtil = new JsonUtil();

        FileReader fileReader = mock(FileReader.class);
        SlackExecutor slackExecutor = mock(SlackExecutor.class);
        MockSlackContainer slack = mockSlack();

        SlackTaskPlugin plugin = new SlackTaskPlugin(jsonUtil, fileReader, slack.slackExecutorFactory);

        DefaultGoPluginApiRequest request = new DefaultGoPluginApiRequest("task", "1.0", "configuration");
        request.setRequestBody("test-request");
        GoPluginApiResponse response = plugin.handle(request);

        assertThat(response.responseBody(),
                is("{" +
                        "\"WebhookUrl\":{\"default-value\":\"\",\"secure\":false,\"required\":true}," +
                        "\"Message\":{\"default-value\":\"\",\"secure\":false,\"required\":false}," +
                        "\"IconOrEmoji\":{\"default-value\":\"\",\"secure\":false,\"required\":false}," +
                        "\"Channel\":{\"default-value\":\"\",\"secure\":false,\"required\":true}," +
                        "\"Color\":{\"default-value\":\"\",\"secure\":false,\"required\":false}," +
                        "\"DisplayName\":{\"default-value\":\"\",\"secure\":false,\"required\":false}," +
                        "\"Title\":{\"default-value\":\"\",\"secure\":false,\"required\":false}," +
                        "\"ColorType\":{\"default-value\":\"None\",\"secure\":false,\"required\":true}," +
                        "\"ChannelType\":{\"default-value\":\"Channel\",\"secure\":false,\"required\":true}" +
                    "}"));
    }

    private static Property prop(String value) {
        return new Property(value, false, false);
    }

    private static Property requiredProp(String value) {
        return new Property(value, false, true);
    }

}