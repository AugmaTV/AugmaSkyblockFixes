package fr.augma.augmaskyblockfix.client.config;

import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import io.github.notenoughupdates.moulconfig.gui.editors.GuiOptionEditorDraggableList;
import io.github.notenoughupdates.moulconfig.processor.MoulConfigProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class DynamicEntityCategory {

	private static final String[] OPTION_FIELDS = {"hitboxEnabled", "hitboxColour", "scaleEnabled", "scaleSize"};

	private static final int ACCORDION_BASE = 500000;

	public static void install(final MoulConfigProcessor<ModConfig> processor, final EntityConfig config, final EntityCatalogue catalogue) {
		config.selectionFrom(catalogue.ids());
		processor.registerConfigEditor(EntityListEditor.class, (option, annotation) -> new GuiOptionEditorDraggableList(option, catalogue.labels(), true, false));

		try {
			final Field selection = EntityConfig.class.getDeclaredField("selection");
			final Field accordionAnchor = EntityEntryConfig.class.getDeclaredField(OPTION_FIELDS[0]);

			final Field apply = EntityConfig.class.getDeclaredField("apply");

			processor.beginCategory(config, selection, "Entities", "Pick the entities to draw a hitbox around or to scale");
			processor.emitOption(config, selection, new DynamicOption("Entities", "Add entities with the arrow, drag to reorder, click the cross to remove"));
			processor.emitOption(config, apply, new DynamicOption("Apply", "Refresh the per entity settings after changing the list"));

			int accordionId = ACCORDION_BASE;
			for (int index = 0; index < catalogue.ids().length; index++) {
				final String id = catalogue.ids()[index];
				final EntityEntryConfig entry = config.getEntries().get(id);
				if (entry == null) {
					continue;
				}

				processor.beginAccordion(entry, accordionAnchor, new DynamicOption(catalogue.labels()[index], id), accordionId);
				for (final String name : OPTION_FIELDS) {
					final Field field = EntityEntryConfig.class.getDeclaredField(name);
					processor.emitOption(entry, field, field.getAnnotation(ConfigOption.class));
				}
				processor.endAccordion();
				accordionId++;
			}

			processor.endCategory();
		} catch (final NoSuchFieldException exception) {
			throw new IllegalStateException("Missing entity config field", exception);
		}
	}

	private record DynamicOption(String name, String desc) implements ConfigOption {

		@Override
		public Class<? extends Annotation> annotationType() {
			return ConfigOption.class;
		}

	}

}