package com.wrcreedrs.games.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;

import com.wrcreedrs.games.GamesLoader;
import com.wrcreedrs.games.LocalPlayer;
import com.wrcreedrs.games.game.event.GameBlockListener;

public class GamespackPlayerListener implements Listener {
	GamesLoader plugin;
	public GamespackPlayerListener(GamesLoader plugin) {
		this.plugin = plugin;
		System.out.println("Player listener initiated!");
	}
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		System.out.println("An PlayerQuitEvent occurred");
		
		plugin.removeLocalPlayer(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		System.out.println("An PlayerInteractEvent occurred");
		Player player = event.getPlayer();
		LocalPlayer lp = plugin.getLocalPlayer(player);
		if (lp.getCurrentGame() != null) {
			GameBlockListener listener = lp.getCurrentGame().blockListener;
			if (listener != null) {
				if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) listener.onLeftMouseClick(event);
				if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) listener.onRightMouseClick(event);
			}
		}
	}
}
