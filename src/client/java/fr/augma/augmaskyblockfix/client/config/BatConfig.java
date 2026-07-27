package fr.augma.augmaskyblockfix.client.config;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.ChromaColour;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorColour;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorSlider;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import lombok.Getter;

@Getter
public class BatConfig {

	@Expose
	@ConfigOption(name = "Enable scale", desc = "Enable or disable the bat model scaling")
	@ConfigEditorBoolean
	public boolean scaleEnabled = false;

	@Expose
	@ConfigOption(name = "Scale size", desc = "Scale multiplier (1.0 = normal size)")
	@ConfigEditorSlider(minValue = 1F, maxValue = 10F, minStep = 0.1F)
	public float scaleSize = 1F;

	@Expose
	@ConfigOption(name = "Enable hitbox", desc = "Enable the bat hitbox")
	@ConfigEditorBoolean
	public boolean hitboxEnabled = false;

	@Expose
	@ConfigOption(name = "Hitbox color", desc = "Bat hitbox color, raise the chroma speed for a rainbow effect")
	@ConfigEditorColour
	public String hitboxColour = ChromaColour.special(0, 255, 255, 0, 0);

	public int getHitboxRgb() {
		return ChromaColour.forLegacyString(this.hitboxColour).getEffectiveColourRGB();
	}

}