package fr.augma.augmaskyblockfix.client.config;

import io.github.notenoughupdates.moulconfig.gui.GuiContext;
import io.github.notenoughupdates.moulconfig.gui.GuiElementComponent;
import io.github.notenoughupdates.moulconfig.platform.MoulConfigScreenComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ModConfigScreen {

	public static Screen create(final Screen parent) {
		return new MoulConfigScreenComponent(Component.literal("AugmaSkyblockFixes Configuration"), new GuiContext(new GuiElementComponent(ModConfig.managed().getEditor())), parent) {
			@Override
			public void removed() {
				super.removed();
				ModConfig.managed().saveToFile();
			}
		};
	}

}