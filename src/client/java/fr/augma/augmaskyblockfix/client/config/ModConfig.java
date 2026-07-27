package fr.augma.augmaskyblockfix.client.config;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.annotations.Category;
import io.github.notenoughupdates.moulconfig.common.text.StructuredText;
import io.github.notenoughupdates.moulconfig.managed.ManagedConfig;
import lombok.Getter;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

@Getter
public class ModConfig extends Config {

	@Expose
	@Category(name = "Dungeon", desc = "Dungeon settings")
	public DungeonConfig dungeon = new DungeonConfig();

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
			MANAGED.getInstance().getMiscellaneous().getCenteredPlants().addObserver((oldValue, newValue) -> Minecraft.getInstance().levelRenderer.allChanged());
		}

	}

}