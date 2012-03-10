package com.wrcreedrs.games;

import org.bukkit.entity.Player;

import com.wrcreedrs.games.game.GameRunnable;

public class LocalPlayer {
	
	Player player;
	GameRunnable currentGame = null;
	public LocalPlayer(Player player) {
		this.player = player;
	}
	public GameRunnable getCurrentGame() {
		return currentGame;
	}
	public void setCurrentGame(GameRunnable newGame) {
		currentGame = newGame;
	}
	public Object getPlayer() {
		return player;
	}
}
