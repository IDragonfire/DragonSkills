package com.github.idragonfire.dragonskills.skills;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.github.idragonfire.dragonskills.DragonSkillsPlugin;
import com.github.idragonfire.dragonskills.api.ActiveSkill;
import com.github.idragonfire.dragonskills.api.SkillResult;
import com.github.idragonfire.dragonskills.utils.DUtils;

public class PvpPort extends ActiveSkill {

	public PvpPort(DragonSkillsPlugin plugin) {
		super(plugin);
	}

	@Override
	public SkillResult use(Player player) {
		World world = player.getWorld();
		List<Player> players = world.getPlayers();
		if (players.size() <= 1) {
			return SkillResult.FAIL;
		}
		HashMap<int[], ChunkStats> playerMap = new HashMap<int[], ChunkStats>();
		int[] key = new int[2];
		for (Player p : players) {
			if (player == p) {
				continue;
			}
			key[0] = p.getLocation().getChunk().getX();
			key[1] = p.getLocation().getChunk().getZ();
			if (!playerMap.containsKey(key)) {
				playerMap.put(key, new ChunkStats(p.getLocation().getChunk()));
			}
			playerMap.get(key).increase();
		}
		List<ChunkStats> chunks = new ArrayList<ChunkStats>(playerMap.values());
		Collections.sort(chunks);
		Chunk mostPlayerChunk = chunks.get(0).getChunk();
		int x = DUtils.nextInt(DUtils.CHUNK_SIDE);
		int z = DUtils.nextInt(DUtils.CHUNK_SIDE);
		Location loc = mostPlayerChunk.getBlock(x, 0, z).getLocation();
		loc = world.getHighestBlockAt(loc).getLocation();
		loc.setYaw(DUtils.nextInt(360));
		player.teleport(loc);
		return SkillResult.SUCESSFULL;
	}

	@Override
	public String getDescription() {
		return "Teleport you to the chunk with the most players in your world";
	}

	private class ChunkStats implements Comparable<ChunkStats> {
		public int players;
		public Chunk chunk;

		public ChunkStats(Chunk chunk) {
			this.players = 0;
			this.chunk = chunk;
		}

		public void increase() {
			this.players++;
		}

		public int getPlayers() {
			return this.players;
		}

		public Chunk getChunk() {
			return this.chunk;
		}

		@Override
		public int compareTo(ChunkStats b) {
			return b.getPlayers() - this.players;
		}
	}

}
