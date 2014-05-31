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

import org.bukkit.ChatColor;

import java.util.ArrayList;

/**
 * Name: JChatPart.java Created: 30 May 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
final class JChatPart {

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
/*
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
    }*/

}

