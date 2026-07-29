package fr.augma.augmaskyblockfix.client.config;

import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import io.github.notenoughupdates.moulconfig.processor.MoulConfigProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DynamicRadialCategory {

	private static final String[] OPTION_FIELDS = {"label", "icon", "command", "remove"};

	private static final int ACCORDION_BASE = 600000;

	public static void install(final MoulConfigProcessor<ModConfig> processor, final RadialConfig config) {
		try {
			final Field anchor = RadialConfig.class.getDeclaredField("entries");
			final Field enabled = RadialConfig.class.getDeclaredField("enabled");
			final Field keybind = RadialConfig.class.getDeclaredField("keybind");
			final Field newShortcut = RadialConfig.class.getDeclaredField("newShortcut");
			final Field add = RadialConfig.class.getDeclaredField("add");
			final Field accordionAnchor = ShortcutConfig.class.getDeclaredField(OPTION_FIELDS[0]);

			processor.beginCategory(config, anchor, "Radial menu", "Command shortcuts on a radial menu");
			processor.emitOption(config, enabled, enabled.getAnnotation(ConfigOption.class));
			processor.emitOption(config, keybind, keybind.getAnnotation(ConfigOption.class));
			processor.emitOption(config, newShortcut, newShortcut.getAnnotation(ConfigOption.class));
			processor.emitOption(config, add, new DynamicOption("Add", "Create the shortcut at the path above"));

			int accordionId = ACCORDION_BASE;
			for (final String path : sorted(config)) {
				final ShortcutConfig entry = config.getEntries().get(path);
				processor.beginAccordion(entry, accordionAnchor, new DynamicOption(indent(path), path), accordionId);
				for (final String name : OPTION_FIELDS) {
					final Field field = ShortcutConfig.class.getDeclaredField(name);
					final ConfigOption option = field.getAnnotation(ConfigOption.class);
					processor.emitOption(entry, field, option != null ? option : new DynamicOption("Remove", "Delete this shortcut and all its children"));
				}
				processor.endAccordion();
				accordionId++;
			}

			processor.endCategory();
		} catch (final NoSuchFieldException exception) {
			throw new IllegalStateException("Missing radial config field", exception);
		}
	}

	private static List<String> sorted(final RadialConfig config) {
		final List<String> paths = new ArrayList<>(config.getEntries().keySet());
		paths.sort(String.CASE_INSENSITIVE_ORDER);
		return paths;
	}

	private static String indent(final String path) {
		return "   ".repeat(RadialConfig.depthOf(path)) + RadialConfig.nameOf(path);
	}

	private record DynamicOption(String name, String desc) implements ConfigOption {

		@Override
		public Class<? extends Annotation> annotationType() {
			return ConfigOption.class;
		}

	}

}