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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

import static org.bukkit.ChatColor.*;

/**
 * The Main JavaPlugin Class, main purpose is to allow multiple plugins to all use this
 * from a single instance, without having to shard it and risk naming conficts.
 * <p>This class will just provide static methods to create new JChat objects. Just
 * Have add a dependancy to your plugins plugin.yml file
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

    /**
     * Instantiates a new JChat Object
     *
     * @return a new instance of {@link org.codemine.jchatter.JChat}, all the doc's can be found there
     */
    public static JChat getJChat() {
        return new JChat();
    }

    /**
     * Instantiates a new JChat Object
     * <p>Messages are built in "parts" where all part share the same formatting and style.
     *
     * @param firstPartText the text can be the first part of a message.
     *                      Bare in mind all text added here will share the same formatting.
     * @return a new instance of {@link org.codemine.jchatter.JChat}, all the doc's can be found there
     */
    public static JChat getJChat(String firstPartText) {
        return new JChat(firstPartText);
    }

    /**
     * Only experimental while testing this will be removed as soon as the basic bugs and features
     * are coded.
     *
     * @param e the e
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        JChat jChat = new JChat("||");
        jChat.color(RED)
          .style(MAGIC)
          .then("ADMIN")
          .color(BLUE)
          .style(ChatColor.UNDERLINE, ChatColor.ITALIC)
          .link("http://jd.bukkit.org/beta/apidocs/overview-summary.html")
          .itemTooltip("&6The Display Name", Arrays.asList("&aFirst Lore Line ", "Second", "", "&bthird"))
          .then("||")
          .color(RED)
          .style(MAGIC);
        //System.out.println(jChat.toJSONString());
        //System.out.println(jChat.toOldMessageFormat());
        if (!jChat.send("Relicum")) {
            getLogger().severe("Unable to send message to player");
        }

    }

}
