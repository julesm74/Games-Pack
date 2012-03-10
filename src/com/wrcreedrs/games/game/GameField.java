package com.wrcreedrs.games.game;

import org.bukkit.World;
import org.bukkit.block.Block;

public class GameField {
	int direction;
	int xTotal, playerXOffs, xStart;
	int yTotal, playerYOffs, yStart;
	int zTotal, playerZOffs, zStart;
	
	public GameField(int xStart, int yStart, int zStart, int xTotal, int yTotal, int zTotal, int playerXOffs, int playerYOffs, int playerZOffs, int direction) {
		this.direction = direction;
		this.xStart = xStart;
		this.yStart = yStart;
		this.zStart = zStart;
		this.xTotal = xTotal;
		this.yTotal = yTotal;
		this.zTotal = zTotal;
		this.playerXOffs = playerXOffs;
		this.playerYOffs = playerYOffs;
		this.playerZOffs = playerZOffs;
	}
	
	private int translateX(int offsX, int offsY, int offsZ) {
		offsX -= playerXOffs; offsY -= playerYOffs; offsZ -= playerZOffs;
		
		if (direction == 2) return (offsX)    + playerXOffs;
		if (direction == 1) return (offsZ)    + playerXOffs;
		if (direction == 0) return (-(offsX)) + playerXOffs;
		if (direction == 3) return (-(offsZ)) + playerXOffs;
		return 0;
	}
	
	private int translateZ(int offsX, int offsY, int offsZ) {
		offsX -= playerXOffs; offsY -= playerYOffs; offsZ -= playerZOffs;
		
		if (direction == 2) return (offsZ   ) + playerZOffs;
		if (direction == 1) return (offsX   ) + playerZOffs;
		if (direction == 0) return (-(offsZ)) + playerZOffs;
		if (direction == 3) return (-(offsX)) + playerZOffs;
		return 0;
	}
	
	public Block getBlock(World w, int offsX, int offsY, int offsZ) {
		int locX = xStart + translateX(offsX, offsY, offsZ);
		int locY = yStart + offsY;
		int locZ = zStart + translateZ(offsX, offsY, offsZ);
		
		return w.getBlockAt(locX, locY, locZ);
	}

	public Block[] getAllBlocks(World w) {
		Block[] list = new Block[xTotal*yTotal*zTotal];
		int i=0;
		for (int x=0; x<xTotal; x++) {
			for (int y=0; y<yTotal; y++) {
				for (int z=0; z<zTotal; z++) {
					list[i++] = getBlock(w, x, y, z);
				}
			}
		}
		return list;
	}
	
	
	
	//direction 0 = north, 1 = east, 2 = south, 3 = west
	//north = -x
	//east  = -z 
	//south = +x
	//west  = +z
}
