package fr.augma.augmaskyblockfix.client.config;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import lombok.Getter;

@Getter
public class MiscellaneousConfig {

	@Expose
	@ConfigOption(name = "Centered plants", desc = "Remove the random offset applied to plants so they stay centered on their block")
	@ConfigEditorBoolean
	public boolean centeredPlants = false;

}