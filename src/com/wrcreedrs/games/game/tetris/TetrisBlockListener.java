package com.wrcreedrs.games.game.tetris;

import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;

import com.wrcreedrs.games.game.event.GameBlockListener;

public class TetrisBlockListener implements GameBlockListener {
	Tetris game;
	
	public static LB[] blocks = new LB[]{
		//UP
		new LB(  7, 13, 19, 0),
		new LB(  8, 13, 19, 0),
		//RIGHT
		new LB(  6, 10, 19, 1),
		new LB(  6, 11, 19, 1),
		new LB(  6, 12, 19, 1),
		//DOWN
		new LB(  7,  9, 19, 2),
		new LB(  8,  9, 19, 2),
		//LEFT
		new LB(  9, 10, 19, 3),
		new LB(  9, 11, 19, 3),
		new LB(  9, 12, 19, 3),
	};
	
	public TetrisBlockListener(Tetris game) {
		this.game = game;
	}
	
	
	public void onRightMouseClick(PlayerInteractEvent event) {
		clicked(event.getClickedBlock());
	}

	public void onLeftMouseClick(PlayerInteractEvent event) {
		clicked(event.getClickedBlock());
	}
	
	public void clicked(Block block) {
		for (LB lb : blocks) {
			if (game.field.getBlock(game.player.getWorld(), lb.x, lb.y, lb.z).equals(block)) {
				switch (lb.ID) {
					case 0:	game.pressedUp = true; break;
					case 2: game.pressedDown = true; break;
					case 1: game.pressedLeft = true; break;
					case 3: game.pressedRight = true; break;
				}
			}
		}
	}
	

	public static class LB {
		public int x,y,z,ID;
		public LB(int x, int y, int z, int ID) {
			this.x = x; this.y = y; this.z = z; this.ID = ID;
		}
	}
}
