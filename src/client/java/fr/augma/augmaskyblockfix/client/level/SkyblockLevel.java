package fr.augma.augmaskyblockfix.client.level;

public class SkyblockLevel {

	private static final int TIER_SIZE = 40;

	private static final int[] TIERS = {0xAAAAAA, 0xFFFFFF, 0xFFFF55, 0x55FF55, 0x00AA00, 0x55FFFF, 0x00AAAA, 0x5555FF, 0xFF55FF, 0xAA00AA, 0xFFAA00, 0xFF5555, 0xAA0000};

	public static int gradientOf(final int level) {
		if (level <= 0) {
			return TIERS[0];
		}

		final int tier = Math.min(level / TIER_SIZE, TIERS.length - 1);
		if (tier == TIERS.length - 1) {
			return TIERS[tier];
		}
		return blend(TIERS[tier], TIERS[tier + 1], (level % TIER_SIZE) / (float) TIER_SIZE);
	}

	private static int blend(final int from, final int to, final float ratio) {
		final int red = channel(from, 16) + Math.round((channel(to, 16) - channel(from, 16)) * ratio);
		final int green = channel(from, 8) + Math.round((channel(to, 8) - channel(from, 8)) * ratio);
		final int blue = channel(from, 0) + Math.round((channel(to, 0) - channel(from, 0)) * ratio);
		return (red << 16) | (green << 8) | blue;
	}

	private static int channel(final int colour, final int shift) {
		return (colour >> shift) & 0xFF;
	}

}
