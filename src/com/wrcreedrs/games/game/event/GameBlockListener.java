package com.wrcreedrs.games.game.event;

import org.bukkit.event.player.PlayerInteractEvent;

public interface GameBlockListener {
	public void onRightMouseClick(PlayerInteractEvent event);
	public void onLeftMouseClick(PlayerInteractEvent event);
}
