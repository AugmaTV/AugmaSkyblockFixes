package fr.augma.augmaskyblockfix.client.radial;

import com.mojang.blaze3d.platform.InputConstants;
import fr.augma.augmaskyblockfix.client.config.ModConfig;
import fr.augma.augmaskyblockfix.client.config.RadialConfig;
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
		final RadialMenuScreen screen = RadialMenuScreen.getCurrent();

		if (down && !wasDown) {
			if (screen != null) {
				if (!radial.isHoldToOpen()) {
					minecraft.setScreenAndShow(null);
				}
			} else if (minecraft.mouseHandler.isMouseGrabbed()) {
				RadialMenuScreen.open("");
			}
		} else if (!down && wasDown && screen != null && radial.isHoldToOpen()) {
			screen.activateHovered();
		}

		wasDown = down;
	}

}