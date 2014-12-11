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
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * JChat message sender, Example class to send pre made JSON messages using the console
 * and the tellraw command. The player is not required to have the tellraw permission.
 * <p>If you use the run_command in a message the command is run as the player so normal permissions apply.
 *
 * @author Relicum
 * @version 0.0.1
 */
public class JChatSender {

    private static final String cmd = "tellraw ";

    private static String prefix;

    /**
     * THIS IS STILL UNDER CONSTRUCTION
     * DO NO USE AS CURRENTLY DOES NOTHING
     *
     * @param pre the pre
     */
    public static void setPrefix(String pre) {
        Validate.notNull(pre, "setPrefix args can not be Null");
        prefix = pre;
    }

    /**
     * Send a JChat Message to the stored player
     * <P>This uses the console and the tellraw command to send the message.
     * The player will still require the correct permissions to run any click events
     * <P>This method is fairly thread safe as it is synchronized
     *
     * @param message the pre made message in JSON format
     * @throws java.lang.NullPointerException      if a player can't be found.
     * @throws org.bukkit.command.CommandException if there was a problem dispatching the command
     */
    public static synchronized void sendToAll(String message) throws NullPointerException, CommandException {
        Validate.notNull(message, "You must pass a JSON formatted message to send to the user");
        for (Player player : Bukkit.getOnlinePlayers()) {

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd + player.getName() + " " + message);
        }
    }

    /**
     * Send a JChat Message to a Player
     * <P>This uses the console and the tellraw command to send the message.
     * The player will still require the correct permissions to run any click events
     * <P>This method is fairly thread safe as it is synchronized
     *
     * @param player  the players string name to send the message to
     * @param message the pre made message in JSON format
     * @throws java.lang.NullPointerException      if the player is not found.
     * @throws org.bukkit.command.CommandException if there was a problem dispatching the command
     */
    public static synchronized void sendToPlayer(String player, String message) throws NullPointerException, CommandException {

        Validate.notNull(player, "You must pass a valid player name to send the message to");
        Validate.notNull(message, "You must pass a JSON formatted message to send to the user");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd + player + " " + message);

    }

    /**
     * Send a JChat Message to a Player using their UUID.
     * <P>This uses the console and the tellraw command to send the message.
     * The player will still require the correct permissions to run any click events
     * <P>This method is fairly thread safe as it is synchronized
     *
     * @param player  the players UUID to send the message to
     * @param message the pre made message in JSON format
     * @throws java.lang.NullPointerException      if the player is not found
     * @throws org.bukkit.command.CommandException if there was a problem dispatching the command
     */
    public static synchronized void sendToPlayer(UUID player, String message) throws NullPointerException, CommandException {

        Validate.notNull(player, "You must pass a valid player UUID to send the message to");
        Validate.notNull(message, "You must pass a JSON formatted message to send to the user");

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd + Bukkit.getPlayer(player) + " " + message);
    }

    /**
     * Send a JChat Message to a Player
     * <P>This uses the console and the tellraw command to send the message.
     * The player will still require the correct permissions to run any click events
     * <P>This method fairly thread safe as it is synchronized
     *
     * @param player  the {@link org.bukkit.entity.Player} to send the message to
     * @param message the pre made JSON message in {@link java.lang.String} format
     * @throws java.lang.NullPointerException      if the player is not found.
     * @throws org.bukkit.command.CommandException if there was a problem dispatching the command
     */
    public static synchronized void sendToPlayer(Player player, String message) throws NullPointerException, CommandException {
        Validate.notNull(player, "You must pass a valid player name to send the message to");
        Validate.notNull(message, "You must pass a JSON formatted message to send to the user");
        sendToPlayer(player.getName(), message);
    }

}

