package com.wrcreedrs.games.game;

import org.bukkit.entity.Player;

import com.wrcreedrs.games.GamesLoader;
import com.wrcreedrs.games.game.tetris.Tetris;

public enum Game {
	TETRIS("Tetris", "Old-school falling blocks!", Tetris.class);

	
	private String name, shortDesc;
	private Class<? extends GameRunnable> mainClass;
	
	private Game(String name, String shortDesc, Class<? extends GameRunnable> mainClass) {
		this.name = name;
		this.shortDesc = shortDesc;
		this.mainClass = mainClass;
	}
	
	public String getShortDescription() {
		// TODO Auto-generated method stub
		return shortDesc;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	
	public GameRunnable start(GamesLoader plugin, Player player) throws InstantiationException, IllegalAccessException {
		GameRunnable game = mainClass.newInstance();
		game.initiate(plugin, this, player);
		
		return game;
	}

	public boolean hasPermission(Player player) {
		return player.hasPermission("gamespack.play." + name.toLowerCase());
	}
}
