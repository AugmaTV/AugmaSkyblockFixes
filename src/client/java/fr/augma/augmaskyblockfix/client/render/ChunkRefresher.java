package fr.augma.augmaskyblockfix.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;

public class ChunkRefresher {

	public static void refreshOffsetSections() {
		final Minecraft minecraft = Minecraft.getInstance();
		final ClientLevel level = minecraft.level;
		if (level == null || minecraft.player == null) {
			return;
		}

		final int viewDistance = minecraft.options.getEffectiveRenderDistance();
		final int centerX = SectionPos.blockToSectionCoord(minecraft.player.getBlockX());
		final int centerZ = SectionPos.blockToSectionCoord(minecraft.player.getBlockZ());
		final int minSectionY = level.getMinSectionY();

		for (int chunkX = centerX - viewDistance; chunkX <= centerX + viewDistance; chunkX++) {
			for (int chunkZ = centerZ - viewDistance; chunkZ <= centerZ + viewDistance; chunkZ++) {
				final LevelChunk chunk = level.getChunk(chunkX, chunkZ);
				final LevelChunkSection[] sections = chunk.getSections();
				for (int index = 0; index < sections.length; index++) {
					if (sections[index].maybeHas(BlockState::hasOffsetFunction)) {
						minecraft.levelRenderer.setSectionDirty(chunkX, minSectionY + index, chunkZ);
					}
				}
			}
		}
	}

}