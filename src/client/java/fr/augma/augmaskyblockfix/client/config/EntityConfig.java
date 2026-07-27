package fr.augma.augmaskyblockfix.client.config;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorText;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import io.github.notenoughupdates.moulconfig.observer.Property;
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class EntityConfig {

	@Expose
	@ConfigOption(name = "Entities", desc = "Comma separated entity ids. Reopen this screen to edit the settings of each one")
	@ConfigEditorText
	public Property<String> entities = Property.of("minecraft:bat");

	@Expose
	public Map<String, EntityEntryConfig> entries = new LinkedHashMap<>();

	@Getter(AccessLevel.NONE)
	private transient Map<EntityType<?>, EntityEntryConfig> resolved;

	public EntityEntryConfig find(final EntityType<?> type) {
		if (this.resolved == null) {
			this.sync();
		}
		return this.resolved.get(type);
	}

	public List<String> sync() {
		final List<String> ids = new ArrayList<>();
		for (final String entry : this.entities.get().split(",")) {
			final String id = entry.trim();
			if (!id.isEmpty() && !ids.contains(id)) {
				ids.add(id);
			}
		}

		this.entries.keySet().retainAll(ids);
		this.resolved = new HashMap<>();
		for (final String id : ids) {
			final EntityType<?> type = resolve(id);
			if (type == null) {
				continue;
			}
			this.resolved.put(type, this.entries.computeIfAbsent(id, key -> new EntityEntryConfig()));
		}
		return ids;
	}

	private static EntityType<?> resolve(final String id) {
		final Identifier identifier = Identifier.tryParse(id);
		if (identifier == null || !BuiltInRegistries.ENTITY_TYPE.containsKey(identifier)) {
			return null;
		}
		return BuiltInRegistries.ENTITY_TYPE.getValue(identifier);
	}

}