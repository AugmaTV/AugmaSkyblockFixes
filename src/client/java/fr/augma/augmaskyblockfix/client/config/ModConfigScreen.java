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

import java.util.Map;

public class ModConfigScreen {

	public static Screen create(final Screen parent) {
		return create(parent, null);
	}

	private static Screen create(final Screen parent, final String selectedCategory) {
		final ManagedConfig<ModConfig> managed = ModConfig.managed();
		final EntityConfig entities = managed.getInstance().getEntities();
		final EntityCatalogue catalogue = EntityCatalogue.build();

		final RadialConfig radial = managed.getInstance().getRadial();

		managed.rebuildConfigProcessor();
		DynamicEntityCategory.install(managed.getProcessor(), entities, catalogue);
		DynamicRadialCategory.install(managed.getProcessor(), radial);

		final MoulConfigEditor<ModConfig> editor = managed.getEditor();
		if (selectedCategory != null) {
			final ProcessedCategory category = managed.getProcessor().getAllCategories().get(selectedCategory);
			if (category != null) {
				editor.setSelectedCategory(category);
			}
		}

		final Runnable reopen = () -> Minecraft.getInstance().setScreenAndShow(create(parent, editor.getSelectedCategory()));

		entities.apply = () -> {
			entities.applySelection(catalogue.ids());
			reopen.run();
		};

		radial.add = () -> {
			final String path = radial.getNewShortcut().get().trim();
			if (!path.isEmpty() && !radial.getEntries().containsKey(path)) {
				radial.getEntries().put(path, new ShortcutConfig());
				radial.getNewShortcut().set("");
				reopen.run();
			}
		};

		for (final Map.Entry<String, ShortcutConfig> shortcut : radial.getEntries().entrySet()) {
			final String path = shortcut.getKey();
			shortcut.getValue().remove = () -> {
				radial.getEntries().keySet().removeIf(key -> key.equals(path) || key.startsWith(path + RadialConfig.SEPARATOR));
				reopen.run();
			};
		}

		return new MoulConfigScreenComponent(Component.literal("AugmaSkyblockFixes Configuration"), new GuiContext(new GuiElementComponent(editor)), parent) {
			@Override
			public void removed() {
				super.removed();
				entities.applySelection(catalogue.ids());
				managed.saveToFile();
			}
		};
	}

}