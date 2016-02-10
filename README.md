Go Slack Task Plugin
====================

This plugin allows [https://www.go.cd/](Go CD) to send custom messages to Slack
from any job. Use this plugin for example to notify about successful/failed deployments
to Slack channel or user.

Configuration
-------------

|Parameter|Description|Required|
|---------|-----------|--------|
|Webhook URL|Slack webhook URL|Yes|
|Channel|Channel to post. Without `#` or `@` prefix|Yes|
|Channel type|Type of the channel (channel or user)|Yes|
|Display name|Sender name (e.g. Go CD)|Yes|
|Title|Message title (e.g. Deployed to production)|Yes|
|Message|Actual message|No|
|Icon or Emoji|Icon URL or Slack emoji (e.g. `https://example.org/icon.png` or `:tada:`)|No|
|Color|Pre-defined colors|Yes|
|Custom color|Custom color as six hex digit code without `#` (e.g. `ff0000` for red). Only when custom color is selected as the color.|No|

### Message formatting

Message fields (display name, title and message) all support environment variables.
Environment variables can be referenced in messages using the dollar symbol: `$ENV_VAR_NAME`.
The plugin currently only support adding the environment variables as is.
Normal bash string manipulation is not supported. Referencing parameters works the
same way it does in Go (e.g. `#{paramName}`).
