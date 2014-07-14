/**
 * JChatter making sending onHover and onEvent chat messages super easy.
 * <p>The final message is sent using the console using Tellraw.
 * <p>The first class to read is @{link JChat} which is there the action takes place.
 *
 * <strong>Brand new feature includes to methods that allow you to code and the entire message section using color code and
 * styles as well as being able to set unlimited placeholders. The plugin will then do the rest for you</strong>
 * <pre>
 * {@code
 *     JChat jChat = new JChat();
jChat.then("&a&oHi %s hope you like the &6&o%s &a&oserver", player.getName(), "Factions")
.tooltipsWithValues("&aHi %s hope you like &b%s server", player.getName(), "Factions"))
.send(player);
 *     }
 * </pre>
 *
 * NB- known limitations is if you try to do the entire line in one go as standard the tooltips and events are only applied
 * to the previous message part. Already part way through a solution that will allow you to have placeholder that refer to tooltips of events.
 *
 * @author Relicum
 * Other developers whos inspired me to create this solution. @Spoonyloony @bobacadodl @dorkrepublic
 * @version 0.0.1
 */
