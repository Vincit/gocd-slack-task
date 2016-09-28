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
|Channel|Channel to post. Channel type prefix (`#` or `@`) is only required if the channel type is `text`.|Yes|
|Channel type|The type of channel (`channel`, `user` or `text`).|Yes|
|Display name|Sender's name (e.g. - Go CD)|Yes|
|Title|Title or subject header of the message (e.g. - Deployed to production)|Yes|
|Message|The actual message|No|
|Message: Slack message formatting|Enables Slack message formatting for the message|No|
|Icon or Emoji|The icon URL or Slack emoji (e.g. - `https://example.org/icon.png` or `:tada:`)|No|
|Color|The color label for the message displayed as a bar on the side. Select `custom` to define any color|Yes|
|Custom color|Defines a custom color as a three or six-hex digit code without `#` (e.g. - `ff0000` for red).|If using custom color|

### Environment Variables and Parameters

GoCD parameters (`#{paramName}`) work on all text fields as this is the default
behaviour for GoCD.

Environment variables can be referenced in all text fields using the dollar symbol (e.g. - `$ENV_VAR_NAME`).
The plugin currently only supports adding the environment variables as is. Escaping the dollar symbol
works with backslash (e.g. - `\$ThisIsNotReplaced`). Normal Bash string manipulation is not supported.

### Message Formatting

It is possible to format the message using [Slack's message formatting options](https://get.slack.help/hc/en-us/articles/202288908-How-can-I-add-formatting-to-my-messages-).
Formatting can be enabled for the message via the _Slack message formatting_ checkbox under
the message input. Display name and title fields don't support message formatting.

License
-------

[http://www.apache.org/licenses/LICENSE-2.0]

Copyright 2015 - 2016 Juha Siponen
