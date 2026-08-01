package fr.augma.augmaskyblockfix.client.radial;

import com.mojang.blaze3d.platform.InputConstants;
import fr.augma.augmaskyblockfix.client.config.ModConfig;
import fr.augma.augmaskyblockfix.client.config.RadialConfig;
import net.minecraft.client.Minecraft;

public class RadialKeybind {

	private static final int MINIMUM_KEY = 32;

	private static boolean wasDown;

	public static void tick(final Minecraft minecraft) {
		final RadialConfig radial = ModConfig.get().getRadial();
		if (!radial.isEnabled() || radial.getKeybind() < MINIMUM_KEY || minecraft.level == null) {
			wasDown = false;
			return;
		}

		final boolean down = InputConstants.isKeyDown(minecraft.getWindow(), radial.getKeybind());

		if (down && !wasDown) {
			if (RadialOverlay.isOpen()) {
				if (!radial.isHoldToOpen()) {
					RadialOverlay.close(true);
				}
			} else if (minecraft.mouseHandler.isMouseGrabbed()) {
				RadialOverlay.open("");
			}
		} else if (!down && wasDown && RadialOverlay.isOpen() && radial.isHoldToOpen()) {
			RadialOverlay.close(true);
		}

		wasDown = down;
	}

}