package com.wrcreedrs.games;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BlockEngine {
	public static void SetPlayerBlock(Player player, Block block, int type, byte data) {
		player.sendBlockChange(block.getLocation(), type, data);
	}
}
