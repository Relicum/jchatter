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

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

import static org.bukkit.ChatColor.*;

/**
 * Name: JChatter.java Created: 30 May 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class JChatter extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {

        getServer().getPluginManager().registerEvents(this, this);

    }

    @Override
    public void onDisable() {

    }

    public static JChat getJChat() {
        return new JChat();
    }

    public static JChat getJChat(String firstPart) {
        return new JChat(firstPart);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        JChat jChat = getJChat("[");
        jChat
          .color(DARK_PURPLE)
          .then("MCOITC")
          .color(AQUA)
          .then("] ")
          .color(DARK_PURPLE)
          .then()
          .color(BLUE)
          .style(BOLD)
          .text("Here is a message in bold")
          .command("/say hello")
          .itemTooltip("&6The Display", Arrays.asList("&aMy First ", "My Second", "", "My third"))
        ;
        System.out.println(jChat.toJSONString());
        jChat.send();

    }

}
