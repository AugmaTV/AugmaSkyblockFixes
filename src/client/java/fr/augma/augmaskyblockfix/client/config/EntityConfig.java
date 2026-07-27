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

import java.util.HashSet;
import java.util.Set;

@Getter
public class EntityConfig {

	@Expose
	@ConfigOption(name = "Enable hitbox", desc = "Draw a box around the listed entities")
	@ConfigEditorBoolean
	public boolean hitboxEnabled = false;

	@Expose
	@ConfigOption(name = "Hitbox entities", desc = "Comma separated entity ids, for example minecraft:bat, minecraft:silverfish")
	@ConfigEditorText
	public Property<String> hitboxEntities = Property.of("minecraft:bat");

	@Expose
	@ConfigOption(name = "Hitbox color", desc = "Hitbox color, raise the chroma speed for a rainbow effect")
	@ConfigEditorColour
	public String hitboxColour = ChromaColour.special(0, 255, 255, 0, 0);

	@Expose
	@ConfigOption(name = "Enable scale", desc = "Scale the model of the listed entities")
	@ConfigEditorBoolean
	public boolean scaleEnabled = false;

	@Expose
	@ConfigOption(name = "Scale entities", desc = "Comma separated entity ids, for example minecraft:bat, minecraft:silverfish")
	@ConfigEditorText
	public Property<String> scaleEntities = Property.of("minecraft:bat");

	@Expose
	@ConfigOption(name = "Scale size", desc = "Scale multiplier (1.0 = normal size)")
	@ConfigEditorSlider(minValue = 1F, maxValue = 10F, minStep = 0.1F)
	public float scaleSize = 1F;

	@Getter(AccessLevel.NONE)
	private transient String parsedHitboxEntities;

	@Getter(AccessLevel.NONE)
	private transient Set<EntityType<?>> hitboxTypes = Set.of();

	@Getter(AccessLevel.NONE)
	private transient String parsedScaleEntities;

	@Getter(AccessLevel.NONE)
	private transient Set<EntityType<?>> scaleTypes = Set.of();

	public int getHitboxRgb() {
		return ChromaColour.forLegacyString(this.hitboxColour).getEffectiveColourRGB();
	}

	public Set<EntityType<?>> getHitboxTypes() {
		final String raw = this.hitboxEntities.get();
		if (!raw.equals(this.parsedHitboxEntities)) {
			this.parsedHitboxEntities = raw;
			this.hitboxTypes = parse(raw);
		}
		return this.hitboxTypes;
	}

	public Set<EntityType<?>> getScaleTypes() {
		final String raw = this.scaleEntities.get();
		if (!raw.equals(this.parsedScaleEntities)) {
			this.parsedScaleEntities = raw;
			this.scaleTypes = parse(raw);
		}
		return this.scaleTypes;
	}

	private static Set<EntityType<?>> parse(final String raw) {
		final Set<EntityType<?>> types = new HashSet<>();
		for (final String entry : raw.split(",")) {
			final Identifier identifier = Identifier.tryParse(entry.trim());
			if (identifier != null && BuiltInRegistries.ENTITY_TYPE.containsKey(identifier)) {
				types.add(BuiltInRegistries.ENTITY_TYPE.getValue(identifier));
			}
		}
		return types;
	}

}