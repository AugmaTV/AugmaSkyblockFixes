package fr.augma.augmaskyblockfix.client.config;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.Accordion;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import lombok.Getter;

@Getter
public class DungeonConfig {

	@Expose
	@Accordion
	@ConfigOption(name = "Bat", desc = "Bat configuration section")
	public BatConfig bat = new BatConfig();

}