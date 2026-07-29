package fr.augma.augmaskyblockfix.client.radial;

import com.mojang.blaze3d.platform.InputConstants;
import fr.augma.augmaskyblockfix.client.config.RadialConfig;
import fr.augma.augmaskyblockfix.client.config.ModConfig;
import net.minecraft.client.Minecraft;

public class RadialKeybind {

	private static boolean wasDown;

	public static void tick(final Minecraft minecraft) {
		final RadialConfig radial = ModConfig.get().getRadial();
		if (!radial.isEnabled() || minecraft.level == null) {
			wasDown = false;
			return;
		}

		final boolean down = InputConstants.isKeyDown(minecraft.getWindow(), radial.getKeybind());
		if (down && !wasDown && minecraft.mouseHandler.isMouseGrabbed()) {
			RadialMenuScreen.open("");
		}
		wasDown = down;
	}

}