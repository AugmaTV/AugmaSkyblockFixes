package fr.augma.augmaskyblockfix.client.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;

import java.awt.Color;

public class ModConfig {

    public static final ConfigClassHandler<ModConfig> HANDLER = ConfigClassHandler.createBuilder(ModConfig.class)
            .id(ResourceLocation.parse("augmaskyblockfix"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("augmaskyblockfix.json"))
                    .build())
            .build();

    public static ModConfig get() {
        return HANDLER.instance();
    }

    // === BAT SCALE ===
    @SerialEntry
    public boolean batScaleEnabled = true;

    @SerialEntry
    public float batScaleSize = 3.0F;

    // === BAT HITBOX ===
    @SerialEntry
    public boolean batHitboxEnabled = true;

    @SerialEntry
    public boolean batHitboxRainbow = true;

    @SerialEntry
    public Color batHitboxColor = new Color(255, 0, 0);

    @SerialEntry
    public int rainbowSpeed = 3000;

}