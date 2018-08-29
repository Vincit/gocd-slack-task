Changelog
===========

2.0
---

 * Add toggle to allow build to pass even if Slack API is down
   
 Thanks `DonoA` for the contribution

#### Breaking Change

 Previously this plugin failed the build if the Slack API was down or unreachable. The default
 behaviour has been changed so that the build is not failed anymore. This can still be turned on
 from the task settings if this is still the desired behaviour.
 
1.3.1
-----

 * Show exception message in GoCD plugin error message if sending message to Slack fails

1.3
---

 * All text fields support environment variables
 * Add new channel type: text
 * Add support for three-digit hex color codes

1.2
---

 * Add support for Slack message formatting in message

1.1
---

 * Add support for any Slack like API (e.g. Rocket.Chat)

1.0 (Initial release)
---------------------

 * Support for
   * sending message to a channel or a user
   * predefined and custom colors
   * Emoticons and custom icons
   * Environmental variables and properties in display name, message and title