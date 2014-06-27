/*
 * The MIT License
 *
 * Copyright (c) 2014 Relicum
 *
 * The following authors supplied code that this Lib is based on @Spoonyloony @bobacadodl @dorkrepublic
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.codemine.jchatter;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.com.google.gson.stream.JsonWriter;
import org.bukkit.entity.Player;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * JChat is a simple fluentAPI for creating Hover and Click Json Strings.
 * <p>It also has built in facilities to send the message to a player or all players or a
 * collection of players. What makes this different from other implementations is that it uses,
 * <strong><ol>
 * <li>NO REFLECTION</li>
 * <li>NO NMS CODE</li>
 * <li>NO USE OF CUSTOM PACKETS</li>
 * </ol>
 * </strong>
 * <p>All messages are built using standard java code and it uses the vanilla minecraft TellRaw command to send the messages
 * from the console. Players require NO extra permissions. Also any Click Event of type run_command use the users
 * standard permissions.
 * <p/>
 * <p>
 *     The instance of player is uses the players string name. Unlike when using {@link org.bukkit.block.CommandBlock}
 *     there is no @a to send to all players. Each message must be send separately.
 * <tt>
 *     Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player + " " + message);
 * </tt>
 * </p>
 * <p>
 * <tt>
 * This class has been expired and built apon code by @bobacadodl and @spoonyloony. Big thanks for them for releasing there code.
 * </tt>
 * </p>
 * <p>
 * <tt>
 * Can include Unicode character support. The format is \\u#### in which # represents the unique unicode number.
 * </tt>
 * </p>
 * As they have done all of JChatter is also allowed to be used in any way you choose.
 * <p>A basic usage example. This will demonstrates a lot of the features and the final line will send the JSON message to the
 * user with the name Relicum.
 * <pre>
 * {@code
 *     JChat jChat = new JChat("[");
 *     jChat.color(RED)
 *     .style(BOLD)
 *     .then("ADMIN")
 *     .color(BLUE)
 *     .style.(UNDERLINE,ITALIC)
 *     .command("/say hello")
 *     .itemTooltip("&6The Display Name", Arrays.asList("&aFirst Lore Line ", "Second", "", "&bthird"))
 *     .then("]")
 *     .color(RED)
 *     .style(BOLD)
 *     .send("Relicum");
 *     }*
 * </pre>
 * <p>To get the JSON string itself, which you can save to disk or store in a Field replace the .send() line with
 * <pre>
 *     {@code
 *     .toJSONString();
 *     }*
 * </pre>
 * <p>This will output the following string which is the full JSON formatted message. As you can see the API makes it
 * much easier to build and no need to know any JSON at all. This string can be saved to disk or to a Field this is a good idea
 * if you will be using the message a lot as it doesn't require the string to be rebuilt each time.For more info on sending
 * pre made messages see {@link JChatSender} this is a simple example class, with synchronized static methods.
 * For most that class will most likely fulfil all your requirements. But once you have the JSON string you can impliment the sending
 * any way you like.
 * <br></br>
 * <pre>
 *  {"text":"","extra":[{"text":"[","color":"red","bold":true},{"text":"ADMIN","color":"blue","underlined":true,"italic":true,
 *  "clickEvent":{"action":"run_command","value":"/say hello"},"hoverEvent":{"action":"show_item","value":"{id:1s,Count:1b,
 *  tag:{display:{Lore:[0:\"§aFirst Lore Line \",1:\"Second\",2:\" \",3:\"§bthird\",],Name:\"§6The Display Name\",},},Damage:0s,}"}},
 *  {"text":"]","color":"red","bold":true}]}
 * </pre>
 *
 * @author Relicum
 * @version 0.0.1
 */
public class JChat {

    private final List<JChatPart> _jChatParts;
    private String _jsonString;
    private boolean _dirty;
    private boolean _save = true;

    /**
     * Instantiates a new JChat Object
     * <p>Messages are built in "parts" where all part share the same formatting and style.
     *
     * @param firstPartText the text can be the first part of a message.
     *                      Bare in mind all text added here will share the same formatting.
     */
    public JChat(final String firstPartText) {

        _jChatParts = new ArrayList<>();
        _jChatParts.add(new JChatPart(firstPartText));
        _jsonString = null;
        _dirty = false;
    }

    /**
     * Instantiates a new JChat Object
     */
    public JChat() {

        _jChatParts = new ArrayList<JChatPart>();
        _jChatParts.add(new JChatPart());
        _jsonString = null;
        _dirty = false;
    }

    /**
     * Text that forms part of the message. Each message part can <strong>ONLY</strong> contain 1 piece of text.
     * <p>Message parts are separate by using the {@link JChat#then()} or {@link JChat#then(Object)}
     *
     * @param text the text that will form this part of the message
     * @return the {@link org.codemine.jchatter.JChat} instance of itself to allow chaining of methods
     */
    public JChat text(String text) {

        JChatPart latest = latest();
        if (latest.hasText()) {
            throw new IllegalStateException("text for this message part is already set");
        }
        latest.text = text;
        _dirty = true;
        return this;
    }

    /**
     * Used to set the color of this part of the message.
     * Any color from the {@link org.bukkit.ChatColor} can be passed
     *
     * @param color the color the text will be displayed in if unset it defaults to white
     * @return the {@link org.codemine.jchatter.JChat} instance of itself to allow chaining of methods
     * @throws java.lang.IllegalArgumentException if the color is not valid
     */
    public JChat color(final ChatColor color) {

        if (!color.isColor()) {
            throw new IllegalArgumentException(color.name() + " is not a color");
        }
        latest().color = color;
        _dirty = true;
        return this;
    }

    /**
     * Used to Style this part of the message.Chat styles are only applied to the current part of the message.
     *
     * @param styles the {@link org.bukkit.ChatColor} any of the avaiable style options can be used.
     *               Pass in one than one style separated with a ',' if you require multiple styles.
     * @return the {@link org.codemine.jchatter.JChat} instance of itself to allow chaining of methods
     * @throws java.lang.IllegalArgumentException if the style is not valid
     */
    public JChat style(ChatColor... styles) {

        for (final ChatColor style : styles) {
            if (!style.isFormat()) {
                throw new IllegalArgumentException(style.name() + " is not a style");
            }
        }
        latest().styles.addAll(Arrays.asList(styles));
        _dirty = true;
        return this;
    }

    /**
     * Opens a file at the specified path.
     * Currently this does nothing think it is part of 1.8
     * Would advise not to use.
     *
     * @param path the path
     * @return the {@link org.codemine.jchatter.JChat} instance of itself to allow chaining of methods
     */
    public JChat file(final String path) {

        onClick("open_file", path);
        return this;
    }

    /**
     * Make the current message part a click able link. Just like on a web page. The text you click on
     * does not need to be a URL anymore. It will use the URL you specify here
     *
     * @param url the url that will be opened in the players browser.
     * @return the {@link org.codemine.jchatter.JChat} instance of itself to allow chaining of methods
     */
    public JChat link(final String url) {

        onClick("open_url", url);
        return this;
    }

    /**
     * Suggest a command when a player clicks the current message part. This is very similar to {@link JChat#command(String)}
     * <p>the only difference being the the command is only added to the users chat input window. Which they then just hit enter
     * to run the command or add in an extra argument
     *
     * @param command the command that the player will be displayed to the player when this message part is clicked.
     * @return the {@link org.codemine.jchatter.JChat} instance of itself to allow chaining of methods
     */
    public JChat suggest(final String command) {

        onClick("suggest_command", command);
        return this;
    }

    /**
     * Run a command when a player clicks the current message part.
     * The command is ran exactly as if the player typed the command themselves.
     * <p>The command will only run if the player has permission to run that command.
     * The command can have any number of arguments. Basically if your command accepts it you can use it here.
     *
     * @param command the command that the player will be run when this message part is clicked.
     *                Need to input the command exactly as if the player was typing it eg "/home" or "/home raid"
     * @return the {@link org.codemine.jchatter.JChat} instance of itself to allow chaining of methods
     */
    public JChat command(final String command) {

        onClick("run_command", command);
        return this;
    }

    /**
     * Achievement tooltip, see Wiki for String ID's
     * <pre>
     * http://minecraft.gamepedia.com/Achievements#List_of_achievements
     * </pre>
     *
     * @param name the achievement ID (starting with "achievement") eg "achievement.theEnd2"
     * @return the {@link org.codemine.jchatter.JChat} instance of itself to allow chaining of methods
     *
     */
    public JChat achievementTooltip(final String name) {

        onHover("show_achievement", "achievement." + name);
        return this;
    }

    /**
     * Item tooltip.
     *
     * @param itemJSON the item jSON
     * @return the {@link org.codemine.jchatter.JChat} instance of itself to allow chaining of methods
     */
    protected JChat itemTooltip(final String itemJSON) {
        onHover("show_item", itemJSON);
        return this;
    }

    /**
     * Item tooltip. This displays a tooltip like an items Display name and Lore to the player when hovering over the text.
     * <p>To use Color or styles use the & character followed by any Minecraft formatting code.
     * To display a blank line in the lore just set the required line to "".
     *
     * @param title the title is the Item Display Title
     * @param lore  the lore formatted as an List<String> as you normally would using ItemMeta
     * @return the {@link org.codemine.jchatter.JChat} instance of itself to allow chaining of methods
     */
    public JChat itemTooltip(final String title, final List<String> lore) {
        Validate.notNull(title, "You have not passed a display title for the itemToolTip");
        Validate.notNull(lore, "You have not passed the lore for the itemTooltip");
        return itemTooltip(makeItemJSON(title, lore));
    }

    /**
     * Tooltip displayed with the onHover event.
     * <p>To add color use the standard minecraft classic color code formatting using the prefix '&'
     * <p>To add multiple lines use '\n' or better still use a List<String> and set the element to " "
     * <p>Can include Unicode character support. The format is \\u#### in which # represents the unique unicode number.
     *
     * @param text the text that will appear when the onHover event is fired.
     * @return the {@link org.codemine.jchatter.JChat} instance of itself to allow chaining of methods
     */
    public JChat tooltip(final String text) {

        return tooltip(text.split("\\n"));
    }

    /**
     * Tooltip displayed with the onHover event.
     * <p>To add color use the standard minecraft classic color code formatting using the prefix '&'
     * <p>To add multiple lines set the element to " ". This will produce a new line for you
     * <p>Can include Unicode character support. The format is \\u#### in which # represents the unique unicode number.
     *
     * @param lines the lines
     * @return the {@link org.codemine.jchatter.JChat} instance of itself to allow chaining of methods
     */
    public JChat tooltip(final List<String> lines) {

        return tooltip((String[]) lines.toArray());
    }

    /**
     * Tooltip displayed with the onHover event.
     * <p>To add color use the standard minecraft classic color code formatting using the prefix '&'
     * <p>To add multiple lines set the relevant array element to " ". This will produce a new line for you.
     * <p>Can include Unicode character support. The format is \\u#### in which # represents the unique unicode number.
     *
     * @param lines the {@link java.lang.String[]} of text that will appear when the onHover event is fired.
     * @return the {@link org.codemine.jchatter.JChat} instance of itself to allow chaining of methods
     */
    public JChat tooltip(final String... lines) {
        Validate.notNull(lines, "Error tooltip does not contain any lines");
        if (lines.length == 1) {
            onHover("show_text", ChatColor.translateAlternateColorCodes('&', lines[0]));
        } else {
            itemTooltip(makeItemJSON(lines[0], Arrays.asList(lines).subList(1, lines.length)));
        }
        return this;
    }

    /**
     * Then add the next JChatPart.
     * Can include Unicode character support. The format is \\u#### in which # represents the unique unicode number.
     *
     * @param obj the obj to start the message off
     * @return the {@link org.codemine.jchatter.JChat} instance of itself to allow chaining of methods
     */
    public JChat then(final Object obj) {

        if (!latest().hasText()) {
            throw new IllegalStateException("previous message part has no text");
        }
        _jChatParts.add(new JChatPart(obj.toString()));
        _dirty = true;
        return this;
    }

    /**
     * Then add the next JChatPart.
     *
     * @return the {@link org.codemine.jchatter.JChat} instance of itself to allow chaining of methods
     */
    public JChat then() {

        if (!latest().hasText()) {
            throw new IllegalStateException("previous message part has no text");
        }
        _jChatParts.add(new JChatPart());
        _dirty = true;
        return this;
    }

    /**
     * Converts the message into a jSON string.
     * There is no need to call this method if you are building the message on the fly to send now just call:
     * <strong>
     * <ol>
     * <li>
     * {@link JChat#send(org.bukkit.entity.Player)}
     * </li>
     * <li>
     * {@link JChat#send(String)}
     * </li>
     * <li>
     * {@link JChat#send()}
     * </li>
     * </ol>
     * </strong>
     * <p/>
     * There is a simple example class {@link org.codemine.jchatter.JChatSender} that you can use to send the message to player/players in the future.
     *
     * @return the JSON string representing the complete message. You can save this string direct to any file
     * which can then be used at a later date to send to players using the console and the Tellraw command. Or {@link org.codemine.jchatter.JChatSender}
     * @throws java.lang.RuntimeException "invalid message" if the message is not valid
     */
    public String toJSONString() {

        if (!_dirty && _jsonString != null) {
            return _jsonString;
        }
        StringWriter string = new StringWriter();
        JsonWriter json = new JsonWriter(string);
        try {
            if (_jChatParts.size() == 1) {
                latest().writeJson(json);
            } else {
                json.beginObject().name("text").value("").name("extra").beginArray();
                for (final JChatPart part : _jChatParts) {
                    part.writeJson(json);
                }
                json.endArray().endObject();
                json.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("invalid message");
        }
        _jsonString = string.toString();
        _dirty = false;
        return _jsonString;
    }

    /**
     * <p>Send the current JChat message to the specified player name.
     * The player must be online to send the message
     * </p>
     *
     * @param player the players string name the message is sent to
     * @return the {@link java.lang.Boolean} true if no errors occured, false if there was a problem
     * @throws org.bukkit.command.CommandException
     */
    public boolean send(String player) {

        if (isOnline(player))
            return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player + " " + toJSONString());
        else
            try {
                throw new Exception(player + "Is not currently online");
            } catch (Exception e) {
                e.printStackTrace();
            }
        return false;
    }

    /**
     * <p>Send the current JChat message to the specified player UUID.
     * The player must be online to send the message
     * </p>
     *
     * @param player the players full UUID
     * @return the {@link java.lang.Boolean} true if no errors occured, false if there was a problem
     * @throws org.bukkit.command.CommandException
     */
    public boolean send(UUID player) {

        if (isOnline(player))
            return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player + " " + toJSONString());
        else
            try {
                throw new Exception(player + "Is not currently online");
            } catch (Exception e) {
                e.printStackTrace();
            }
        return false;
    }

    /**
     * <p>Send the current JChat message to the specified {@link org.bukkit.entity.Player}.
     * The player must be online to send the message
     * </p>
     *
     * @param player the player the message is sent to
     * @return the {@link java.lang.Boolean} true if no errors occured, false if there was a problem
     * @throws java.lang.Exception if the player is not found on line.
     */
    public boolean send(Player player) throws Exception {
        Validate.notNull(player, "To send a JChat message you must pass a valid Player object");
        if (player.isOnline())
            return send(player.getName());
        else
            try {
                throw new Exception(player.getName() + "Is not currently online");
            } catch (Exception e) {
                e.printStackTrace();
            }
        return false;
    }

    // public abstract JChat save();

    /**
     * Send the current JChat message to all online players
     * <p>Unfortunately unlike command blocks Tellraw from the console
     * requires a player name, you can't use say @a to send to all. It is also
     * not possible to send a broadcast due to that not currently accepting JSON
     * chat components
     * <p/>
     * //TODO Add in fully functional error checking and reporting
     *
     * @return the {@link java.lang.Boolean} true if no errors occured, false if there was a problem
     */
    public boolean send() {
        boolean dirty = false;

        for (Player player : Bukkit.getOnlinePlayers()) {
            dirty = Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + toJSONString());
        }
        return dirty;
    }

    /**
     * Checks if the player is currently online Object can either be the players name or their {@link java.util.UUID}
     *
     * @param player to check online status
     * @return true if the argument was valid and the player is online false if not.
     */
    private boolean isOnline(Object player) {

        if ((player instanceof String)) {

            if (((String) player).length() < 16)

                return Bukkit.getPlayer((String) player).isOnline();
        }

        if (player instanceof UUID) {

            return Bukkit.getPlayer((UUID) player).isOnline();
        }

        return false;
    }

    /**
     * Get a copy of the message in the Old style message syntax.
     * <p>This strips out all the JSON formatting and events, and return
     * the message still with full color and style formatted included.
     *
     * @return the string containing the message in the old Format
     */
    public String toOldMessageFormat() {

        StringBuilder result = new StringBuilder();
        for (JChatPart part : _jChatParts) {
            result.append(part.color).append(part.text);
        }
        return result.toString();
    }

    /**
     * Clear and resets all Field variables.
     * can be useful if you are making messages to save, no don't need to
     * instantiates a new instance of JChat to create another message
     */
    public void clear() {

        _jChatParts.clear();
        _jChatParts.add(new JChatPart());
        _jsonString = null;
        _dirty = false;
    }

    private JChatPart latest() {

        return _jChatParts.get(_jChatParts.size() - 1);
    }


    /**
     * Creates not a JSON string as the name would suggest but a string formatted like item NBT data
     *
     * @param title is the Item Display Title
     * @param lore  of Type List<String> an as you normally would using ItemMeta.
     * @return string representing either the itemTooltip or a multi line tooltip.
     */
    private String makeItemJSON(final String title, final List<String> lore) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lore.size(); i++) {
            sb.append(i)
                    .append(':')
                    .append("\"");
            if (lore.get(i).isEmpty())
                sb.append(" ");
            else
                sb.append(ChatColor.translateAlternateColorCodes('&', lore.get(i)));
            sb.append("\",");
        }
        return String.format("{id:1s,Count:1b,tag:" +
                "{display:" +
                "{Lore:" +
                "[%s]" +
                ",Name:\"%s\",}" +
                ",}" +
                ",Damage:0s,}", sb.toString(), ChatColor.translateAlternateColorCodes('&', title));
    }

    @Deprecated
    private String makeMultilineTooltip(final String[] lines) {

        StringWriter string = new StringWriter();
        JsonWriter json = new JsonWriter(string);
        try {
            json.beginObject().name("id").value(1);
            json.name("tag").beginObject().name("display").beginObject();
            json.name("Name").value("\\u00A7f" + lines[0].replace("\"", "\\\""));
            json.name("Lore").beginArray();
            for (int i = 1; i < lines.length; i++) {
                final String line = lines[i];
                json.value(line.isEmpty() ? " " : line.replace("\"", "\\\""));
            }
            json.endArray().endObject().endObject().endObject();
            json.close();
        } catch (Exception e) {
            throw new RuntimeException("invalid tooltip");
        }
        return string.toString();
    }

    private void onClick(final String name, final String data) {

        final JChatPart latest = latest();
        latest.clickActionName = name;
        latest.clickActionData = data;
        _dirty = true;
    }

    private void onHover(final String name, final String data) {

        final JChatPart latest = latest();
        latest.hoverActionName = name;
        latest.hoverActionData = data;
        _dirty = true;
    }

    /**
     * Creates the individual message part
     */
    private final class JChatPart {

        ChatColor color = ChatColor.WHITE;
        ArrayList<ChatColor> styles = new ArrayList<ChatColor>();
        String clickActionName = null, clickActionData = null,
                hoverActionName = null, hoverActionData = null;
        String text = null;

        JChatPart(final String text) {

            this.text = text;
        }

        JChatPart() {

        }

        boolean hasText() {

            return text != null;
        }

        JsonWriter writeJson(JsonWriter json) {

            try {
                json.beginObject().name("text").value(text);
                json.name("color").value(color.name().toLowerCase());
                for (final ChatColor style : styles) {
                    String styleName;
                    switch (style) {
                        case MAGIC:
                            styleName = "obfuscated";
                            break;
                        case UNDERLINE:
                            styleName = "underlined";
                            break;
                        default:
                            styleName = style.name().toLowerCase();
                            break;
                    }
                    json.name(styleName).value(true);
                }
                if (clickActionName != null && clickActionData != null) {
                    json.name("clickEvent")
                            .beginObject()
                            .name("action").value(clickActionName)
                            .name("value").value(clickActionData)
                            .endObject();
                }
                if (hoverActionName != null && hoverActionData != null) {
                    json.name("hoverEvent")
                            .beginObject()
                            .name("action").value(hoverActionName)
                            .name("value").value(hoverActionData)
                            .endObject();
                }
                return json.endObject();
            } catch (Exception e) {
                e.printStackTrace();
                return json;
            }
        }

    }
}

