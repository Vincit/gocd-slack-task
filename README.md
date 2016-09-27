Go Slack Task Plugin
====================

This plugin allows [Go CD](https://www.go.cd/) to send custom messages to Slack
from any job. Use this plugin for example to notify about successful/failed deployments
to the Slack channel or user.

Installation
------------

 1. Download latest plugin JAR from [releases](https://github.com/Vincit/gocd-slack-task/releases)
 2. Place the JAR to GoCD's external plugin directory (`<go-server-location>/plugins/external`)
 3. Restart GoCD server

Configuration
-------------

|Parameter|Description|Required|
|---------|-----------|--------|
|Webhook URL|Slack webhook URL|Yes|
|Channel|Channel to post. Channel type prefix (`#` or `@`) is only required if channel type is `text`.|Yes|
|Channel type|Type of the channel (`channel`, `user` or `text`).|Yes|
|Display name|Sender name (e.g. Go CD)|Yes|
|Title|Message title (e.g. Deployed to production)|Yes|
|Message|Actual message|No|
|Message: Slack message formatting|Enable Slack message formatting for the message|No|
|Icon or Emoji|Icon URL or Slack emoji (e.g. `https://example.org/icon.png` or `:tada:`)|No|
|Color|Pre-defined colors|Yes|
|Custom color|Custom color as a six hex digit code without `#` (e.g. `ff0000` for red). Only when custom color is selected as the color.|No|

### Environment variables and parameters

GoCD parameters (`#{paramName}`) work on all text fields as this is the default
behaviour fo GoCD.

Environment variables can be referenced in all text fields using the dollar symbol: `$ENV_VAR_NAME`.
The plugin currently only supports adding the environment variables as is.
Normal Bash string manipulation is not supported.

### Message formatting

It is possible to format the message using [Slack's message formatting options](https://get.slack.help/hc/en-us/articles/202288908-How-can-I-add-formatting-to-my-messages-).
Formatting can be enabled for the message via the _Slack message formatting_ checkbox under
the message input. Display name and title fields don't support message formatting.

License
-------

[http://www.apache.org/licenses/LICENSE-2.0]

Copyright 2015 - 2016 Juha Siponen
