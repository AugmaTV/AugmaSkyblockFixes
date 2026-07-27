package fr.augma.augmaskyblockfix.client.config;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.Accordion;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.world.entity.EntityType;

@Getter
public class EntityConfig {

	@Expose
	@Accordion
	@ConfigOption(name = "Entity 1", desc = "First entity slot")
	public EntityEntryConfig first = EntityEntryConfig.create("minecraft:bat");

	@Expose
	@Accordion
	@ConfigOption(name = "Entity 2", desc = "Second entity slot")
	public EntityEntryConfig second = new EntityEntryConfig();

	@Expose
	@Accordion
	@ConfigOption(name = "Entity 3", desc = "Third entity slot")
	public EntityEntryConfig third = new EntityEntryConfig();

	@Expose
	@Accordion
	@ConfigOption(name = "Entity 4", desc = "Fourth entity slot")
	public EntityEntryConfig fourth = new EntityEntryConfig();

	@Expose
	@Accordion
	@ConfigOption(name = "Entity 5", desc = "Fifth entity slot")
	public EntityEntryConfig fifth = new EntityEntryConfig();

	@Getter(AccessLevel.NONE)
	private transient EntityEntryConfig[] slots;

	public EntityEntryConfig find(final EntityType<?> type) {
		for (final EntityEntryConfig entry : this.slots()) {
			if (entry.getType() == type) {
				return entry;
			}
		}
		return null;
	}

	private EntityEntryConfig[] slots() {
		if (this.slots == null) {
			this.slots = new EntityEntryConfig[]{this.first, this.second, this.third, this.fourth, this.fifth};
		}
		return this.slots;
	}

}