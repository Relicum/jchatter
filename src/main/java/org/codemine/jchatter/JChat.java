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
 * <p>All messages are built using standard Bukkit API code and it uses TellRaw command to send the messages
 * from the console. Players require NO extra permissions. Also any Click Event of type run_command use the users
 * standard permissions.
 * <p/>
 * This class has been expired and built apon code by @bobacadodl and @spoonyloony. Big thanks for them for releasing there code.
 * As they have done all of JChatter is also allowed to be used in any way you choose.
 *
 * @author Relicum
 * @version 0.0.1
 */
public class JChat {

    private final List<JChatPart> _jChatParts;
    private String _jsonString;
    private boolean _dirty;
    private boolean _save = true;

    public JChat(final String firstPartText) {

        _jChatParts = new ArrayList<>();
        _jChatParts.add(new JChatPart(firstPartText));
        _jsonString = null;
        _dirty = false;
    }

    public JChat() {

        _jChatParts = new ArrayList<JChatPart>();
        _jChatParts.add(new JChatPart());
        _jsonString = null;
        _dirty = false;
    }

    public JChat text(String text) {

        JChatPart latest = latest();
        if (latest.hasText()) {
            throw new IllegalStateException("text for this message part is already set");
        }
        latest.text = text;
        _dirty = true;
        return this;
    }

    public JChat color(final ChatColor color) {

        if (!color.isColor()) {
            throw new IllegalArgumentException(color.name() + " is not a color");
        }
        latest().color = color;
        _dirty = true;
        return this;
    }

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

    public JChat file(final String path) {

        onClick("open_file", path);
        return this;
    }

    public JChat link(final String url) {

        onClick("open_url", url);
        return this;
    }

    public JChat suggest(final String command) {

        onClick("suggest_command", command);
        return this;
    }

    public JChat command(final String command) {

        onClick("run_command", command);
        return this;
    }

    public JChat achievementTooltip(final String name) {

        onHover("show_achievement", "achievement." + name);
        return this;
    }

    public JChat itemTooltip(final String itemJSON) {
        System.out.println(itemJSON);
        onHover("show_item", itemJSON);
        return this;
    }

    public JChat itemTooltip(final String title, final List<String> lore) {
        Validate.notNull(title, "You have not passed a display title for the itemToolTip");
        Validate.notNull(lore, "You have not passed the lore for the itemTooltip");
        return itemTooltip(makeItemJSON(title, lore));
    }

    public JChat tooltip(final String text) {

        return tooltip(text.split("\\n"));
    }

    public JChat tooltip(final List<String> lines) {

        return tooltip((String[]) lines.toArray());
    }

    public JChat tooltip(final String... lines) {

        if (lines.length == 1) {
            onHover("show_text", lines[0]);
        } else {
            itemTooltip(makeMultilineTooltip(lines));
        }
        return this;
    }

    public JChat then(final Object obj) {

        if (!latest().hasText()) {
            throw new IllegalStateException("previous message part has no text");
        }
        _jChatParts.add(new JChatPart(obj.toString()));
        _dirty = true;
        return this;
    }

    public JChat then() {

        if (!latest().hasText()) {
            throw new IllegalStateException("previous message part has no text");
        }
        _jChatParts.add(new JChatPart());
        _dirty = true;
        return this;
    }

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

    public boolean send(String player) {

        String cmd = "tellraw ";
        String message = toJSONString();

        try {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd + player + " " + message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean send(Player player) {
        Validate.notNull(player, "To send a JChat message you must pass a valid Player object");
        return send(player.getName());
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
     * @return the true if no errors occured, false if there was a problem
     */
    public boolean send() {
        boolean dirty = false;
        for (Player player : Bukkit.getOnlinePlayers()) {
            dirty = send(player.getName());
        }
        return dirty;
    }

    public String toOldMessageFormat() {

        StringBuilder result = new StringBuilder();
        for (JChatPart part : _jChatParts) {
            result.append(part.color).append(part.text);
        }
        return result.toString();
    }

    private JChatPart latest() {

        return _jChatParts.get(_jChatParts.size() - 1);
    }

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

/*        String s = "{id:1s,Count:1b,tag:" +
                     "{display:" +
                     "{Lore:" +
                     "[%s]" +
                     ",Name:\"%s\",}" +
                     ",}" +
                     ",Damage:0s,}";*/
        return String.format("{id:1s,Count:1b,tag:" +
                               "{display:" +
                               "{Lore:" +
                               "[%s]" +
                               ",Name:\"%s\",}" +
                               ",}" +
                               ",Damage:0s,}", sb.toString(), ChatColor.translateAlternateColorCodes('&', title));
    }

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

