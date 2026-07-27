package fr.augma.augmaskyblockfix.client.config;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public record EntityCatalogue(String[] ids, String[] labels) {

	public static EntityCatalogue build() {
		final List<Identifier> keys = new ArrayList<>(BuiltInRegistries.ENTITY_TYPE.keySet());
		keys.sort(Comparator.comparing(EntityCatalogue::label, String.CASE_INSENSITIVE_ORDER));

		final String[] ids = new String[keys.size()];
		final String[] labels = new String[keys.size()];
		for (int index = 0; index < keys.size(); index++) {
			ids[index] = keys.get(index).toString();
			labels[index] = label(keys.get(index));
		}
		return new EntityCatalogue(ids, labels);
	}

	private static String label(final Identifier identifier) {
		return BuiltInRegistries.ENTITY_TYPE.getValue(identifier).getDescription().getString();
	}

}