JChat - A Custom Implementation of FancyMessageAPI
============================================

JChat is a custom implantation of the plugin FancyMessageAPI by @dorkrepublic which can be found at https://github.com/dorkrepublic/FancyMessageAPI.

```
The difference being this version does not use any reflection/NMS code to create OR send the messages. A lot of the message creation code was written by @dorkrepublic so it's only right he gets the recognition deserved.
```

This plugin uses @dorkrepublic fluentAPI to chain the creation of messages quickly. The messages are then sent using the Console and the TellRaw command.

Include is a simple method to send to a single player or send to all players.

There is also an abstract save and load method which can be used to save and load messages that are used often. This makes it more efficient due to not having to build the JSON message each time.
  