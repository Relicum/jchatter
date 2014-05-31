JChatter - For sending JSON Chat messages
============================================

'''WORK IN PROGRESS'''

JChatter is a custom implementation of various over Libs by @dorkrepublic @Spoonyloony @bobacadodl 

<blockquote>
The difference being this version does not use any reflection/NMS code to create OR send the messages.
</blockquote>

This plugin uses a fluentAPI to chain the creation of messages quickly. The messages are then sent using the Console and the TellRaw command.

Included is a simple method to send to a single player or send to all players.

There is also an abstract save and load method which can be used to save and load messages that are used often. This makes it more efficient due to not having to build the JSON message each time.

  