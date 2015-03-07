JChatter 1.1.0 - Sending JSON Chat messages
============================================

Spigot or Bukkit 1.8 and 1.8.3 Only
===


``JChatter is a custom implementation of various other Libs by @dorkrepublic @Spoonyloony @bobacadodl, many thanks for doing some of the ground work.`` 


`Recently added the ability to passing in color codes and styles directly to messages and tooltips as well as using unlimited placeholders. The plugin
will take care of the rest allowing for very rapid development.`


<blockquote>
The difference being this version does not use any reflection/NMS code or Packet manipulation to create OR send the messages.
</blockquote>


Also included is the ability to add unicode symbols, using a enum call JSymbols, this works in the same way as the ChatColor class. Currently this is
still a WIP and is liable to change very shortly so please don't rely on it for production code, the documentation will display what each of the current symbols looks
like provided you have an up to date browser.

This plugin uses a fluentAPI to chain the creation of messages quickly. The messages are then sent using the Console and the TellRaw command.

Included is a simple method to send to a single player or send to all players.

There is also an abstract save and load method which can be used to save and load messages that are used often. This makes it more efficient due to not having to build the JSON message each time.


**Pull requests gladly accepted, as well as feature requests, though a can't promise to impliment all requests**

  
