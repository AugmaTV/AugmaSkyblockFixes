package fr.augma.augmaskyblockfix.client.config;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.ChromaColour;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorColour;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorSlider;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorText;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import io.github.notenoughupdates.moulconfig.observer.Property;
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;

@Getter
public class EntityEntryConfig {

	@Expose
	@ConfigOption(name = "Entity", desc = "Entity id, for example minecraft:bat. Leave empty to disable this slot")
	@ConfigEditorText
	public Property<String> entity = Property.of("");

	@Expose
	@ConfigOption(name = "Enable hitbox", desc = "Draw a box around this entity")
	@ConfigEditorBoolean
	public boolean hitboxEnabled = false;

	@Expose
	@ConfigOption(name = "Hitbox color", desc = "Hitbox color, raise the chroma speed for a rainbow effect")
	@ConfigEditorColour
	public String hitboxColour = ChromaColour.special(0, 255, 255, 0, 0);

	@Expose
	@ConfigOption(name = "Enable scale", desc = "Scale the model of this entity")
	@ConfigEditorBoolean
	public boolean scaleEnabled = false;

	@Expose
	@ConfigOption(name = "Scale size", desc = "Scale multiplier (1.0 = normal size)")
	@ConfigEditorSlider(minValue = 1F, maxValue = 10F, minStep = 0.1F)
	public float scaleSize = 1F;

	@Getter(AccessLevel.NONE)
	private transient String parsedEntity;

	@Getter(AccessLevel.NONE)
	private transient EntityType<?> type;

	public static EntityEntryConfig create(final String entity) {
		final EntityEntryConfig config = new EntityEntryConfig();
		config.entity = Property.of(entity);
		return config;
	}

	public EntityType<?> getType() {
		final String raw = this.entity.get();
		if (!raw.equals(this.parsedEntity)) {
			this.parsedEntity = raw;
			this.type = resolve(raw);
		}
		return this.type;
	}

	public int getHitboxRgb() {
		return ChromaColour.forLegacyString(this.hitboxColour).getEffectiveColourRGB();
	}

	private static EntityType<?> resolve(final String raw) {
		final Identifier identifier = Identifier.tryParse(raw.trim());
		if (identifier == null || !BuiltInRegistries.ENTITY_TYPE.containsKey(identifier)) {
			return null;
		}
		return BuiltInRegistries.ENTITY_TYPE.getValue(identifier);
	}

}