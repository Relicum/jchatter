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

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The Main JavaPlugin Class, main purpose is to allow multiple plugins to all use this
 * from a single instance, without having to shard it and risk naming conflicts.
 * <p>This class will just provide static methods to create new JChat objects. Just
 * Have add a dependency to your plugins plugin.yml file and call either of the static methods
 * below to obtain a new instance of JChat.
 *
 * @author Relicum
 * @version 0.0.1
 */
public class JChatter extends JavaPlugin {

    @Override
    public void onEnable() {

        getLogger().info("JChatter was written by Relicum but includes work by dorkrepublic Spoonyloony bobacadodl");
        getLogger().info("More details can be found here https://github.com/Relicum/jchatter");

/*        jChat = new JChat();

        jChat
                .coloredText("&a&lHelp me");
                jChat.then()
                .coloredText("&a&l&oMore&c stuff")
                        .then()
                        .text("peter pan")
                        .color(ChatColor.BLUE)
                        .command("/say heeelooooooo")
                        .itemTooltip("&6My Title", Arrays.asList("&4first", " ", "&b this is the second"));*/


        // System.out.println(jChat.toJSONString());
    }

    @Override
    public void onDisable() {

    }


    /**
     * This method is purely for my testing it is not live.
     */

    private void join(PlayerJoinEvent e) {


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


}
