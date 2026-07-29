package fr.augma.augmaskyblockfix.client.radial;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

public class RadialActions {

	public static void run(final String command) {
		final ClientPacketListener connection = Minecraft.getInstance().getConnection();
		if (connection == null) {
			return;
		}

		final String trimmed = command.trim();
		if (trimmed.isEmpty()) {
			return;
		}
		connection.sendCommand(trimmed.startsWith("/") ? trimmed.substring(1) : trimmed);
	}

}