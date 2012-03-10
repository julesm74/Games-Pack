package com.wrcreedrs.games.game.tetris;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.wrcreedrs.games.BlockEngine;
import com.wrcreedrs.games.game.GameField;
import com.wrcreedrs.games.game.GameRunnable;
import com.wrcreedrs.games.game.tetris.TetrisBlockListener.LB;

public class Tetris extends GameRunnable {
	GameField field;
	int[][] renderedScreen;
	int[][] screen;
	
	TetrisBlock currentblock = null;
	int[][] solidblocks;
	
	final static int tickTime = 30;
	int ticksleft = tickTime;
	public boolean pressedUp = false, pressedDown = false, pressedLeft = false, pressedRight = false;
	
	public void tick() {
		ticksleft--;
		if (ticksleft <= 0) { 
			if (currentblock == null) {
				currentblock = new TetrisBlock(this, TetrisBlock.Shape.random());
			}
			ticksleft = tickTime;
			
			if (pressedUp)    { pressedUp    = false; currentblock.tryRotate(); }
			if (pressedRight) { pressedRight = false; currentblock.tryMoveRight(); }
			if (pressedLeft)  { pressedLeft  = false; currentblock.tryMoveLeft(); }
			
			if (currentblock.canMoveDown()) {
				currentblock.moveDown();
			} else {
				System.out.println("Solidized");
				currentblock.solidize();
				currentblock = null;
			}
		}
		//Render part
		for (int x=0; x<solidblocks.length; x++) {
			for (int y=0; y<solidblocks[x].length; y++) {
				screen[x][y] = solidblocks[x][y];
			}
		}
		if (currentblock != null) currentblock.render();
		
		updateScreen();
	}
	
	private Block getBlockOnScreen(int x, int y) {
		int xGet = 14-x;
		int yGet = 18-y;
		int zGet = 0;
		
		return field.getBlock(player.getWorld(), xGet, yGet, zGet);
	}
	
	private void updateScreen() {
		for (int x=0; x<screen.length; x++) {
			for (int y=0; y<screen[x].length; y++) {
				int rendered = renderedScreen[x][y];
				int current = screen[x][y];
				
				if (current != rendered) {
					BlockEngine.SetPlayerBlock(player, getBlockOnScreen(x, y), 35, (byte)current);
					renderedScreen[x][y] = current;
				}
			}
		}
	}
	
	public void onStart() {
	/* Ready the game field */
		Location ploc = this.player.getLocation();
		int playerX = ploc.getBlockX();
		int playerY = ploc.getBlockY();
		int playerZ = ploc.getBlockZ();
		
		float yaw = ploc.getYaw();
		while (yaw < 0) yaw += 360;
		while (yaw > 360) yaw -= 360;
		
		int direction = (yaw>=45 && yaw<135)?1:(yaw>=135 && yaw < 225)?2:(yaw>=225 && yaw<315)?3:0;
		
		field = new GameField(playerX-8, playerY-10, playerZ-20, 16, 20, 25, 8, 10, 20, direction);
		
		for (int x=0; x<16; x++) {
			for (int y=0; y<20; y++) {
				for (int z=0; z<25; z++) {
					if (z==0 && (x>=0 && x<=15 && y>=0 && y<=19)) {//Blocks on other side of field that player faces
						if (x>0 && x<15 && y>0 && y<19) {//inside of those blocks
							BlockEngine.SetPlayerBlock(player, field.getBlock(player.getWorld(), x, y, z), 35, (byte)15);
						} else {//outside of it
							BlockEngine.SetPlayerBlock(player, field.getBlock(player.getWorld(), x, y, z), 89, (byte)0);
						}
					} else if (x==0||x==15 || y==0||y==19 || z==0||z==24) {//Outside border
						BlockEngine.SetPlayerBlock(player, field.getBlock(player.getWorld(), x, y, z), 20, (byte)0);
					} else if (y==9 && (z<24 && z>18 && x>5 && x<10)) {//Where player stands
						BlockEngine.SetPlayerBlock(player, field.getBlock(player.getWorld(), x, y, z), 20, (byte)0);
					} else {//all other bloicks, aka air
						BlockEngine.SetPlayerBlock(player, field.getBlock(player.getWorld(), x, y, z), 0 , (byte)0);
					}
				}
			}
		}
		for (LB lb : TetrisBlockListener.blocks) {
			BlockEngine.SetPlayerBlock(player, field.getBlock(player.getWorld(), lb.x, lb.y, lb.z), 42, (byte)0);
		}
	/* -- -- -- -- -- -- -- */
	/* Registering game data*/	
		screen = new int[14][18];
		renderedScreen = new int[14][18];
		solidblocks = new int[14][18];
		
		for (int xx=0; xx<14; xx++) {
			for (int yy=0; yy<18; yy++) {
				screen[xx][yy] = 15;
				renderedScreen[xx][yy] = 15;
				solidblocks[xx][yy] = 0;
			}
		}
		
		TetrisBlockListener blockEventListener = new TetrisBlockListener(this);
		addBlockEvents(blockEventListener);
	}
	


	public void onStop() {
		/* Clear up the game field */
		for (Block b : field.getAllBlocks(player.getWorld()))
			BlockEngine.SetPlayerBlock(player, b, b.getTypeId(), b.getData());
		/* -- -- -- -- -- -- -- -- */
	}

	public void gameOver() {
		// TODO Auto-generated method stub
		
	}
}
