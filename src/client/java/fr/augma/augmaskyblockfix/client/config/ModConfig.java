package fr.augma.augmaskyblockfix.client.config;

import com.google.gson.annotations.Expose;
import fr.augma.augmaskyblockfix.client.render.ChunkRefresher;
import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.annotations.Category;
import io.github.notenoughupdates.moulconfig.common.text.StructuredText;
import io.github.notenoughupdates.moulconfig.managed.ManagedConfig;
import lombok.Getter;
import net.fabricmc.loader.api.FabricLoader;

@Getter
public class ModConfig extends Config {

	@Expose
	@Category(name = "Entities", desc = "Entity rendering settings")
	public EntityConfig entities = new EntityConfig();

	@Expose
	@Category(name = "Miscellaneous", desc = "Miscellaneous settings")
	public MiscellaneousConfig miscellaneous = new MiscellaneousConfig();

	public static ManagedConfig<ModConfig> managed() {
		return Holder.MANAGED;
	}

	public static ModConfig get() {
		return Holder.MANAGED.getInstance();
	}

	@Override
	public StructuredText getTitle() {
		return StructuredText.of("AugmaSkyblockFixes");
	}

	private static final class Holder {

		private static final ManagedConfig<ModConfig> MANAGED = ManagedConfig.create(FabricLoader.getInstance().getConfigDir().resolve("augmaskyblockfix.json").toFile(), ModConfig.class);

		static {
			MANAGED.getInstance().getMiscellaneous().getCenteredPlants().addObserver((oldValue, newValue) -> ChunkRefresher.refreshOffsetSections());
		}

	}

}