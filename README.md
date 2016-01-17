Go Slack Task Plugin
====================

This plugin allows [https://www.go.cd/](Go CD) to send custom messages to Slack
from any job. A common use case could be to make Go notify where an artifact has been deployed.

Configuration
-------------

* Webhook URL (required): Slack webhook URL
* Channel (required): Channel to post. Without `#` or `@` prefix
* Channel type (required): Type of the channel (channel or user)
* Display name: Sender name (e.g. Go CD)
* Title: Message title (e.g. Deployed to production)
* Message: Actual message
* Icon or Emoji: Icon URL or Slack emoji (e.g. `https://example.org/icon.png` or `:tada:`)
* Color (required): Pre-defined colors
* Custom color: Custom color as six hex digit code without `#` (e.g. `ff0000` for red). Only when custom color is selected as the color.

### Message formatting

Message fields (display name, title and message) all support environment variables.
Environment variables can be referenced in messages using the dollar symbol: `$ENV_VAR_NAME`.
The plugin currently only support adding the environment variables as is.
Substring feature is not supported. Referencing parameters works the
same way it does in Go.
