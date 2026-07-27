package fr.augma.augmaskyblockfix.client.config;

import com.google.gson.annotations.Expose;
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
	public Map<String, EntityEntryConfig> entries = new LinkedHashMap<>();

	@Getter(AccessLevel.NONE)
	@EntityListEditor
	public transient List<Integer> selection = new ArrayList<>();

	@Getter(AccessLevel.NONE)
	private transient Map<EntityType<?>, EntityEntryConfig> resolved;

	public EntityEntryConfig find(final EntityType<?> type) {
		if (this.resolved == null) {
			this.resolve();
		}
		return this.resolved.get(type);
	}

	public List<Integer> selectionFrom(final String[] catalogue) {
		this.selection.clear();
		for (int index = 0; index < catalogue.length; index++) {
			if (this.entries.containsKey(catalogue[index])) {
				this.selection.add(index);
			}
		}
		return this.selection;
	}

	public void applySelection(final String[] catalogue) {
		final List<String> ids = new ArrayList<>();
		for (final int index : this.selection) {
			if (index >= 0 && index < catalogue.length) {
				ids.add(catalogue[index]);
			}
		}

		this.entries.keySet().retainAll(ids);
		for (final String id : ids) {
			this.entries.computeIfAbsent(id, key -> new EntityEntryConfig());
		}
		this.resolve();
	}

	public void resolve() {
		this.resolved = new HashMap<>();
		for (final Map.Entry<String, EntityEntryConfig> entry : this.entries.entrySet()) {
			final Identifier identifier = Identifier.tryParse(entry.getKey());
			if (identifier != null && BuiltInRegistries.ENTITY_TYPE.containsKey(identifier)) {
				this.resolved.put(BuiltInRegistries.ENTITY_TYPE.getValue(identifier), entry.getValue());
			}
		}
	}

	public static String[] catalogue() {
		return BuiltInRegistries.ENTITY_TYPE.keySet().stream().map(Identifier::toString).sorted().toArray(String[]::new);
	}

}