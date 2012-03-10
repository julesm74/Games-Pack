package com.wrcreedrs.games.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.wrcreedrs.games.GamesLoader;
import com.wrcreedrs.games.game.event.GameBlockListener;

public class GameRunnable implements Runnable {
	GamesLoader plugin;
	Game gametype;
	int timer = -1;
	public Player player;
	
	public GameBlockListener blockListener = null;
	
	public GameRunnable() {}
	public void tick() {}
	public void onStart() {}
	public void onStop() {}
	
	public void setPause(boolean flag) {
		state = flag?State.PAUSED:State.RUNNING;
	}

	public void run() {
		if (state.equals(State.RUNNING)) tick();
	}
	
	public void stop() {
		Bukkit.getScheduler().cancelTask(timer);
		timer = -1;
		
		state = State.STOPPED;
		onStop();
	}
	
	public void start() throws Exception {
		if (state.equals(State.NOT_INITIALISED)) throw new Exception("GAME NOT YET INITIALIZED!");
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, this, 1, 1);
		
		state = State.RUNNING;
		onStart();
	}
	
	public String getName() {
		return gametype.getName();
	}
	
	public void initiate(GamesLoader plugin, Game gametype, Player player) {
		this.plugin = plugin;
		this.gametype = gametype;
		this.player = player;
		
		state = State.NOT_STARTED;
	}
	
	State state = State.NOT_INITIALISED;
	
	public static enum State {
		NOT_INITIALISED, NOT_STARTED, RUNNING, PAUSED, STOPPED;
	}
	
	public void addBlockEvents(GameBlockListener blockListener) {
		this.blockListener = blockListener;
	}
	
}
