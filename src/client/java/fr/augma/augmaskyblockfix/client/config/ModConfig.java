package fr.augma.augmaskyblockfix.client.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;

import java.awt.Color;

@Getter
@Setter
public class ModConfig {

    public static final ConfigClassHandler<ModConfig> HANDLER = ConfigClassHandler.createBuilder(ModConfig.class)
            .id(ResourceLocation.parse("augmaskyblockfix"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config).setPath(FabricLoader.getInstance().getConfigDir().resolve("augmaskyblockfix.json")).build())
            .build();

    public static ModConfig get() {
        return HANDLER.instance();
    }

    // === BAT SCALE ===
    @SerialEntry
    private boolean batScaleEnabled = false;

    @SerialEntry
    private float batScaleSize = 1.0F;

    // === BAT HITBOX ===
    @SerialEntry
    private boolean batHitboxEnabled = false;

    @SerialEntry
    private boolean batHitboxRainbow = false;

    @SerialEntry
    private Color batHitboxColor = new Color(255, 0, 0);

    @SerialEntry
    private int rainbowSpeed = 3000;

    public Color getBatHitboxColor() {
        if (this.isBatHitboxRainbow()) {
            final int rainbowSpeed = ModConfig.get().getRainbowSpeed();
            final int color = Color.HSBtoRGB((float) (System.currentTimeMillis() % rainbowSpeed) / rainbowSpeed, 0.8F, 0.8F);
            return new Color(color);
        }
        return this.batHitboxColor;
    }

}