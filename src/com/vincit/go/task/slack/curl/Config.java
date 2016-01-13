/*************************GO-LICENSE-START*********************************
 * Copyright 2014 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *************************GO-LICENSE-END***********************************/

package com.vincit.go.task.slack.curl;

import java.util.Map;

public class Config {
    private final String channel;
    private final String title;
    private final String message;
    private final String iconOrEmoji;

    public Config(Map config) {
        iconOrEmoji = getValue(config, SlackTaskPlugin.ICON_OR_EMOJI);
        title = getValue(config, SlackTaskPlugin.TITLE);
        message = getValue(config, SlackTaskPlugin.MESSAGE);
        channel = getValue(config, SlackTaskPlugin.CHANNEL);
    }

    private String getValue(Map config, String property) {
        return (String) ((Map) config.get(property)).get("value");
    }

    public String getChannel() {
        return channel;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getIconOrEmoji() {
        return iconOrEmoji;
    }
}