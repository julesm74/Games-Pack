package com.wrcreedrs.games.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.wrcreedrs.games.GamesLoader;
import com.wrcreedrs.games.LocalPlayer;
import com.wrcreedrs.games.game.Game;

public class CommandHandler {
	public static enum Command {
		Games, Play, StopPlay;
		
		public static Command get(String commandName) {
			for (Command c : Command.values()) {
				if (c.name().equalsIgnoreCase(commandName)) return c;
			}
			return null;
		}
	}
	
	GamesLoader plugin;
	
	public CommandHandler(GamesLoader plugin) {
		this.plugin = plugin;
	}
	
	Command curcommand = null;
	Player curplayer = null;
	
	public boolean handle(String commandName, Player player, String[] args) {
		try {
			curcommand = Command.get(commandName);
			curplayer = player;
			if (curcommand == null) return true;
			
			switch (curcommand) {
			case Games:
				returnMessage(ChatColor.GREEN, "Current supported games are: ");
				for (Game game : Game.values()) {
					returnMessage(ChatColor.GREEN, game.getName() + " - " + game.getShortDescription());
				}
				break;
				
			case Play:
				Game requested = null;
				for (Game game : Game.values()) {
					if (args[0].equalsIgnoreCase(game.getName())) {
						requested = game;
					}
				}
				
				if (requested == null) {
					returnErrorMessage("No game found by the name '" + requested + "'");
					return true;
				}
				if (requested.hasPermission(player)) {
					LocalPlayer LP = plugin.getLocalPlayer(player);
					if (LP.getCurrentGame() != null) {
						returnErrorMessage("You already are playing a game. To stop playing, use /stopplay");
						return true;
					} else {
						try {
							LP.setCurrentGame(requested.start(plugin, player));
							LP.getCurrentGame().start();
							returnMessage(ChatColor.GREEN, "Successfull started a game of " + requested.getName());
						} catch (Exception e) {
							returnErrorMessage("Tried to start a game, but an exception happened.");
							returnErrorMessage("Please contact the server administrator for this.");
						}
					}
				} else {
					returnErrorMessage("You don't have the permissions to start a game of '" + requested.getName() + "'");
					return true;
				}
				break;
				
			case StopPlay:
				LocalPlayer LP = plugin.getLocalPlayer(player);
				
				if (LP.getCurrentGame() == null) {
					returnErrorMessage("You aren't playing a game!");
					return true;
				} else {
					String gameName = LP.getCurrentGame().getName();
					LP.getCurrentGame().stop();
					LP.setCurrentGame(null);
					returnMessage(ChatColor.GREEN, "You stopped playing '" + gameName + "'.");
				}
				break;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			returnErrorMessage("Invalid arguments!");
			return false;
		}
		return true;
	}
	
	public void returnErrorMessage(String msg) {
		returnMessage(ChatColor.RED, msg);
	}

	private void returnMessage(ChatColor color, String msg) {
		if (color == null) color = ChatColor.WHITE;
		curplayer.sendMessage(ChatColor.RED + GamesLoader.tag + color + msg);
	}
}
