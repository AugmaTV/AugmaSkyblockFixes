package fr.augma.augmaskyblockfix.client.config;

import io.github.notenoughupdates.moulconfig.gui.GuiContext;
import io.github.notenoughupdates.moulconfig.gui.GuiElementComponent;
import io.github.notenoughupdates.moulconfig.gui.MoulConfigEditor;
import io.github.notenoughupdates.moulconfig.managed.ManagedConfig;
import io.github.notenoughupdates.moulconfig.platform.MoulConfigScreenComponent;
import io.github.notenoughupdates.moulconfig.processor.ProcessedCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class ModConfigScreen {

	public static Screen create(final Screen parent) {
		return create(parent, null);
	}

	private static Screen create(final Screen parent, final String selectedCategory) {
		final ManagedConfig<ModConfig> managed = ModConfig.managed();
		final EntityConfig entities = managed.getInstance().getEntities();
		final String[] catalogue = EntityConfig.catalogue();

		managed.rebuildConfigProcessor();
		DynamicEntityCategory.install(managed.getProcessor(), entities, catalogue);

		final MoulConfigEditor<ModConfig> editor = managed.getEditor();
		if (selectedCategory != null) {
			final ProcessedCategory category = managed.getProcessor().getAllCategories().get(selectedCategory);
			if (category != null) {
				editor.setSelectedCategory(category);
			}
		}

		final List<Integer> snapshot = new ArrayList<>(entities.getSelection());

		return new MoulConfigScreenComponent(Component.literal("AugmaSkyblockFixes Configuration"), new GuiContext(new GuiElementComponent(editor)), parent) {
			@Override
			public void tick() {
				super.tick();
				if (!entities.getSelection().equals(snapshot)) {
					entities.applySelection(catalogue);
					Minecraft.getInstance().setScreenAndShow(create(parent, editor.getSelectedCategory()));
				}
			}

			@Override
			public void removed() {
				super.removed();
				entities.applySelection(catalogue);
				managed.saveToFile();
			}
		};
	}

}