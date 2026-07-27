package fr.augma.augmaskyblockfix.client.config;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import io.github.notenoughupdates.moulconfig.observer.Property;
import lombok.Getter;

@Getter
public class MiscellaneousConfig {

	@Expose
	@ConfigOption(name = "Centered plants", desc = "Remove the random offset applied to plants so they stay centered on their block")
	@ConfigEditorBoolean
	public Property<Boolean> centeredPlants = Property.of(false);

	public boolean isCenteredPlants() {
		return this.centeredPlants.get();
	}

}