/*
 * The MIT License
 *
 * Copyright (c) 2014 Relicum
 *
 * The following authors supplied some of the code that this Lib is based on @Spoonyloony @bobacadodl @dorkrepublic
 * As per their License I have licensed this under the same license and freedoms that I was granted. Please included
 * ALL past and present contributors if you wish to use this plugin.
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

import com.google.gson.stream.JsonWriter;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.StringWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JChat is a simple fluentAPI for creating Hover and Click Json chat messages.
 * <p>It also has built in facilities to send the message to a player or all players or a
 * collection of players. What makes this different from other implementations is that it uses,
 * <p>
 * <tt>
 * This class has been inspired and built using some ideas and code by @bobacadodl and @spoonyloony and @dorkrepublic.
 * Big thanks for them for releasing their code. Without them I wouldn't of had the inspiration to customise this plugin.
 * </tt>
 * </p>
 * <strong><ol>
 * <li>NO REFLECTION</li>
 * <li>NO NMS CODE</li>
 * <li>NO USE OF CUSTOM PACKETS</li>
 * </ol>
 * <strong>This also supports unlimited number of events on a single message</strong>
 * </strong>
 * <p>All messages are built using standard java code and it uses the vanilla minecraft TellRaw command to send the messages
 * from the console. Players require NO extra permissions. Also any Click Event of type run_command use the users
 * standard permissions.
 * <p/>
 * <p>
 * The instance of player below is using the players string name, you can also use a player UUID or the player object itself. Unlike when using {@link org.bukkit.block.CommandBlock}
 * there is no @a to send to all players. Each message must be sent separately.
 * <tt>
 * Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player + " " + message);
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
 *     }
 * </pre>
 * <p>To get the JSON string itself, which you can save to disk or store in a Field replace the .send() line with
 * <pre>
 *     {@code
 *     .toJSONString();
 *     }
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
    private Pattern _pattern = Pattern.compile("%s");

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
     * tooltipsWithValues extended method to create a tooltip which allows color codes and substitute values to be passed.
     * <p>Color codes or styles many be passed in here using the '&' followed by a valid format code. They can be added in any order you like.
     * <p>The message can contain as many place holders as you like place holders in the messages must be '%s' and you must
     * are required to have the same number of values to match the number of placeholders. Failure to do so will and it will
     * throw an MissingFormatArgumentException
     * <p/>
     * <p>Values should be passed as the 2nd argument as a list of strings. The order you add them will be the order they are used to replace
     * placeholders.
     * <p/>
     * <p>Like standard tooltips the onHover event will be applied to the text part that immediately proceeds it. While this function allow you
     * to input an entire line of text and styles all at once, it is still splitting them up to message parts, this is not a design fault, it is
     * the way the JSON message format requires.
     * <p>If you do not need to use placeholders then do not use this method use the standard tooltips. Failure to do this will
     * cause MissingFormatArgumentException to be thrown as the method is expecting it. See below for an example usage of this method.
     * <p>
     * <strong>player</strong> is an instance of {@link org.bukkit.entity.Player}. You are not required to use a player object to send the message,
     * you can use the players name, the players UUID or if left blank it will send to all players. Please note you will need to roughly follow the
     * example below, especially on the instance creation do not pass in any arguments. As mentioned above the following onHover event is applied to
     * the last text part. In this case the "server" word. if you wish to have the effect applied to a different part of the message you will need to
     * split up the .tooltipsWithValues the second example demonstrates this. This will add the effect to the first .then chat part.
     * <p>
     * <pre>
     * {@code
     *     JChat jChat = new JChat();
     *     jChat.then("&a&oHi %s hope you like the &6&o%s &a&oserver", player.getName(), "Factions")
     *     .tooltipsWithValues("&aHi %s hope you like &b%s server", player.getName(), "Factions"))
     *     .send(player);
     *     }
     * </pre>
     * </p>
     * <p>
     * <pre>
     * {@code
     *     JChat jChat = new JChat();
     *     jChat.then("&a&oHi %s hope you like the ", player.getName())
     *     .tooltipsWithValues("&aHi %s hope you like &b%s server", player.getName(), "Factions"))
     *     .then("&6&o%s &a&oserver","Factions")
     *     .send(player);
     *     }
     * </pre>
     * </p>
     * <p>
     * <tt>NOTE CURRENTLY ONLY SINGLE LINE TOOLTIPSWITHVALUES IN THIS FORMAT ARE SUPPORTED AT THIS TIME, MULTI LINE WILL BE ADDED SOON</tt>
     * </p>
     * <p/>
     * <p>Currently it has a limitation of only being able to pass 2 ChatColor objects in a row. They can be one color and style OR 2 styles OR just one of either.
     * this would be valid in the message part. "&a&oHello" This would display green and italic text. This is what I mean when you can only pass to ChatColor objects
     * in a row. This is invalid <strong>"&a&o&lHello"</strong> The total message can have as many Chatcolors as you like provided they are not added more than 2 in a row.
     * I will soon be changing this to remove this limitation but it was hard enough with just the 2 options, allowing the developer complete freedom to the order they are
     * added. These to are equal <strong>"&a&oHello"</strong> OR <strong>"&o&aHello"</strong> both result in identical formatting without the developer indicating in any way
     * what order they are adding them.
     * </p>
     *
     * @param text the text that will be used for the tooltip message. Placeholders MUST be <strong>%s</strong>
     * @param subs the subs the values that are used to replace placeholders in the message. You must pass the exact number of values as you have placeholders.
     * @return the {@link org.codemine.jchatter.JChat} instance of itself to allow chaining of methods
     * @throws MissingFormatArgumentException the missing format argument exception
     */
    public JChat tooltipsWithValues(String text, String... subs) throws MissingFormatArgumentException {
        Validate.notNull(text, "The message can not be null");
        Matcher matcher = _pattern.matcher(text);
        int count = 0;
        while (matcher.find())
            count++;

        if (count == 0 || count != subs.length)
            throw new MissingFormatArgumentException("Error: The number of values do not match the number of placeholders");

        onHover("show_text", ChatColor.translateAlternateColorCodes('&', String.format(text, subs)));
        return this;

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
     * Tooltip displayed with the onHover event, allowing color and style codes and placeholders to be used.
     * <p>For full usage instructions see {@link org.codemine.jchatter.JChat#tooltipsWithValues(String, String...)} as the functionality
     * is almost identical. Except this is for adding the message text body not a tooltip !
     *
     * @param message the text that will be used for the tooltip message. Placeholders MUST be <strong>%s</strong>
     * @param subs    the subs the values that are used to replace placeholders in the message. You must pass the exact number of values as you have placeholders.
     * @return the {@link org.codemine.jchatter.JChat} instance of itself to allow chaining of methods
     * @throws IllegalArgumentException the illegal argument exception
     */
    public JChat then(String message, String... subs) throws IllegalArgumentException {
        Validate.notNull(message, "The message can not be null");
        Matcher matcher = _pattern.matcher(message);
        int count = 0;
        while (matcher.find())
            count++;

        if (count == 0 || count != subs.length)
            throw new MissingFormatArgumentException("Error: The number of values do not match the number of placeholders");

        message = String.format(message, subs);
        //  System.out.println("Message length is currently " + message.length());
        //  System.out.println(message);
        splitPartsOnColor(message);


        return this;
    }

    /**
     * splitPartsOnColor is used to sort and format the input from {@link org.codemine.jchatter.JChat#then(String, String...)}.
     * This is for internal use only.
     *
     * @param message the message that is to be sorted and formatted.
     * @throws IllegalArgumentException
     */
    private void splitPartsOnColor(String message) throws IllegalArgumentException {

        char[] b = message.toCharArray();
        List<Character> finds = new ArrayList<>();
        List<Integer> codePoints = new ArrayList<>();

        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {

                finds.add(b[i + 1]);
                codePoints.add(i + 1);
            }
        }

        int max = codePoints.size();
        // System.out.println("The length of points is " + max);
        //If no code points found means no color codes to parse so just set the message and return
        if (max == 0) {
            text(message);
            finds.clear();
            codePoints.clear();
            return;
        } else {
            int point = 0;
            boolean lastDouble = false;
            for (int i = 0; i <= max - 1; i++) {
                //System.out.println("i is now " + i);
                if (lastDouble) {
                    //     System.out.println("i is being incremented");
                    i++;

                }
                point = codePoints.get(i);
                //  System.out.println("i =" + i + "codepoint = " + point);

                //Must be a single and the last color or style option in the message
                if (i == max - 1) {
                    //   System.out.println("Are we getting to last single");

                    text(message.substring(point + 1));

                    //Test if the character is valid
                    if (isColor(String.valueOf(finds.get(i)))) {
                        color(ChatColor.getByChar(finds.get(i)));
                    } else {
                        style(ChatColor.getByChar(finds.get(i)));
                    }

                    // System.out.println("Quiting from last single");
                    finds.clear();
                    codePoints.clear();
                    return;
                }

                //Must have a chat color following a chat style
                if (point + 2 == (codePoints.get(i + 1))) {
                    //   System.out.println("Must be reaching here and i is" + i);
                    if (i + 1 >= max - 1) {
                        //   System.out.println("About to run the double args if it is the last parts");

                        text(message.substring(point + 3));

                    } else {
                        //    System.out.println("About to run the double args");

                        text(message.substring(point + 3, codePoints.get(i + 2) - 1));

                    }

                    boolean i1 = isColor(String.valueOf(finds.get(i)));
                    boolean i2 = isColor(String.valueOf(finds.get(i + 1)));
                    //Check if both are chat styles
                    if (!i1 && !i2) {
                        style(ChatColor.getByChar(finds.get(i)), ChatColor.getByChar(finds.get(i + 1)));
                    }
                    //check first arg
                    if (i1) {
                        color(ChatColor.getByChar(finds.get(i)));
                    } else {
                        style(ChatColor.getByChar(finds.get(i)));
                    }
                    //check 2nd arg
                    if (i2) {
                        color(ChatColor.getByChar(finds.get(i + 1)));
                    } else {
                        style(ChatColor.getByChar(finds.get(i + 1)));
                    }
                    if (i + 1 >= max - 1) {
                        //    System.out.println("Quiting as this double was the last part");
                        finds.clear();
                        codePoints.clear();
                        return;
                    }

                    if (i + 1 >= max)
                        lastDouble = false;
                    else
                        lastDouble = true;

                    //Only add a new textPart if i + 1 does not make i >= max length -1
                    if (!(i + 1 >= max - 1)) {
                        then();
                    }
                    //   System.out.println("Must be a double");
                }
                //Must be a single chat color or style
                else {

                    text(message.substring(point + 1, codePoints.get(i + 1) - 1));

                    //Test if the character is valid
                    if (isColor(String.valueOf(finds.get(i)))) {
                        color(ChatColor.getByChar(finds.get(i)));
                    } else {
                        style(ChatColor.getByChar(finds.get(i)));
                    }


                    lastDouble = false;
                    if (!(i >= max - 1)) {
                        then();
                    }
                    //   System.out.println("Must be a single");
                }
            }
        }
    }


    /**
     * Tests to see if the character is a valid color or style format
     *
     * @param c the string character to test
     * @return boolean true and a valid color was found false and a valid style was found
     * @throws java.lang.IllegalArgumentException if the character is neither a valid color od style
     */
    private boolean isColor(String c) throws IllegalArgumentException {
        String styles = "KkLlMmNnOoRr";
        String colors = "0123456789AaBbCcDdEeFf";
        if (colors.contains(c))
            return true;
        if (styles.contains(c))
            return false;
        throw new IllegalArgumentException("The character is not a color or a style character");
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
     * <p>This is marked as depreciated due to Bukkit marking it as that, this is also very
     * inefficient way to look up players as they are index by UUID now.
     *
     * @param player the players string name the message is sent to
     * @return the {@link java.lang.Boolean} true if no errors occured, false if there was a problem
     * @throws java.lang.RuntimeException
     */
    @Deprecated
    public boolean send(String player) throws RuntimeException {
        Player p;
        try {
            p = Bukkit.getPlayer(player);
            if (p.isOnline())
                return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + p.getName() + " " + toJSONString());
        } catch (Exception e) {
            throw new RuntimeException(player + "Unable to locate the player with the string name of " + player + " or they are not on line");
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
     * @throws java.lang.RuntimeException
     */
    public boolean send(UUID player) throws RuntimeException {
        Player p;
        try {
            p = Bukkit.getPlayer(player);
            if (p.isOnline())
                return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + p.getName() + " " + toJSONString());
        } catch (Exception e) {
            throw new RuntimeException(player + "Unable to locate the player with the UUID of " + player.toString() + " or they are not on line");
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
            return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + toJSONString());
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
     * Checks if the player with the specified {@link java.util.UUID} is currently online.
     *
     * @param player {@link java.util.UUID} to check online status
     * @return true if the argument was valid and the player is online, false if not.
     */
    private boolean isOnline(UUID player) {

        return Bukkit.getPlayer(player).isOnline();

    }

    /**
     * Checks if the player with the specified name is currently online.
     * <p>This is marked as depreciated due to Bukkit marking it as that, this is also very
     * inefficient way to look up players as they are index by UUID now.
     *
     * @param player the players string name to check online status
     * @return true if the argument was valid and the player is online, false if not.
     */
    @Deprecated
    private boolean isOnline(String player) {
        Validate.isTrue(player.matches("^[A-Za-z0-9_]{2,16}$"), "Invaild player name, must be between 2 and 16 characters");

        return Bukkit.getPlayer(player).isOnline();

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
     * Creates not a JSON string as the name would suggest but a string formatted partly in Json but also item NBT data.
     * This information ws gain directly off the Minecraft Wiki.
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
    /**
     * @deprecated due incompatibility with other methods
     * @see {@link org.codemine.jchatter.JChat#makeItemJSON(String, java.util.List)} for alternative method
     * @throws java.lang.Exception to pre warn that the method is not available.
     */
    private String makeMultilineTooltip(final String[] lines) throws Exception {
        throw new Exception("This method is no long accessible please use makeItemJSON instead");
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

