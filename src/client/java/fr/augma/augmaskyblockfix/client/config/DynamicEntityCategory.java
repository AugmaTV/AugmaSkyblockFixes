package fr.augma.augmaskyblockfix.client.config;

import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import io.github.notenoughupdates.moulconfig.processor.MoulConfigProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public class DynamicEntityCategory {

	private static final String[] OPTION_FIELDS = {"hitboxEnabled", "hitboxColour", "scaleEnabled", "scaleSize"};

	private static final int ACCORDION_BASE = 500000;

	public static void install(final MoulConfigProcessor<ModConfig> processor, final EntityConfig config) {
		final List<String> ids = config.sync();
		if (ids.isEmpty()) {
			return;
		}

		try {
			final Field anchor = EntityConfig.class.getDeclaredField("entries");
			final Field accordionAnchor = EntityEntryConfig.class.getDeclaredField(OPTION_FIELDS[0]);
			processor.beginCategory(config, anchor, "Per entity", "Hitbox and scale settings for each listed entity");

			int accordionId = ACCORDION_BASE;
			for (final String id : ids) {
				final EntityEntryConfig entry = config.getEntries().get(id);
				if (entry == null) {
					continue;
				}

				processor.beginAccordion(entry, accordionAnchor, new DynamicOption(id, "Settings for " + id), accordionId);
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