package fr.augma.augmaskyblockfix.client.config;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.annotations.Category;
import io.github.notenoughupdates.moulconfig.common.text.StructuredText;
import io.github.notenoughupdates.moulconfig.managed.ManagedConfig;
import lombok.Getter;
import net.fabricmc.loader.api.FabricLoader;

@Getter
public class ModConfig extends Config {

	private static final ManagedConfig<ModConfig> MANAGED = ManagedConfig.create(FabricLoader.getInstance().getConfigDir().resolve("augmaskyblockfix.json").toFile(), ModConfig.class);

	@Expose
	@Category(name = "Dungeon", desc = "Dungeon settings")
	public DungeonConfig dungeon = new DungeonConfig();

	public static ManagedConfig<ModConfig> managed() {
		return MANAGED;
	}

	public static ModConfig get() {
		return MANAGED.getInstance();
	}

	@Override
	public StructuredText getTitle() {
		return StructuredText.of("AugmaSkyblockFixes");
	}

}