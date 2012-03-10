package com.wrcreedrs.games;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.wrcreedrs.games.command.CommandHandler;
import com.wrcreedrs.games.game.GameRunnable;
import com.wrcreedrs.games.listeners.*;

public class GamesLoader extends JavaPlugin {
	public static final String tag = "[Games Pack] ";
	public HashMap<Player, LocalPlayer> localplayers;
	
	Listener blockListener = new GamespackBlockListener(this);
	Listener playerListener = new GamespackPlayerListener(this);
	
	CommandHandler commandHandler;
	@Override
	public void onEnable() {
		commandHandler = new CommandHandler(this);
		localplayers = new HashMap<Player, LocalPlayer>();
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(playerListener, this);
		pm.registerEvents(blockListener, this);
	}
	
	@Override
	public void onDisable() {
		for (LocalPlayer lp : localplayers.values()) {
			removeLocalPlayer(lp);
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) { 
		if (commandHandler != null) {
			String cmd = command.getName();
			
			if (sender instanceof Player) {
				Player player = (Player)sender;
				commandHandler.handle(cmd, player, args);
			} else {
				if (CommandHandler.Command.get(cmd) != null) {
					sender.sendMessage(tag + "This plugin doesn't support console commands.");
				}
			}
		}
		return true;
	}

	public LocalPlayer getLocalPlayer(Player player) {
		if (localplayers.get(player) != null) {
			return localplayers.get(player);
		}
		LocalPlayer lp = new LocalPlayer(player);
		localplayers.put(player, lp);
		return lp;
	}

	public void removeLocalPlayer(Player player) {
		if (localplayers.get(player) == null) return;
		removeLocalPlayer(localplayers.get(player));
	}
	
	public void removeLocalPlayer(LocalPlayer lp) {
		GameRunnable game = lp.getCurrentGame();
		if (game != null) {
			game.stop();
		}
		
		localplayers.remove(lp.getPlayer());
	}
}
