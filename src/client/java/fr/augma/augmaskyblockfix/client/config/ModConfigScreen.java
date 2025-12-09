package fr.augma.augmaskyblockfix.client.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.Color;

public class ModConfigScreen {

    public static Screen create(Screen parent) {
        final ModConfig config = ModConfig.get();
        final ModConfig defaults = new ModConfig();

        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("AugmaSkyblockFixes Configuration"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("Bat Settings"))
                        .tooltip(Component.literal("Bat display configuration"))

                        // === GROUPE SCALE ===
                        .group(OptionGroup.createBuilder()
                                .name(Component.literal("Scale"))
                                .description(OptionDescription.of(Component.literal("Scaling the bat model")))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.literal("Enable scale"))
                                        .description(OptionDescription.of(Component.literal("Enable or disable the bat model scaling")))
                                        .binding(defaults.isBatScaleEnabled(), () -> config.isBatScaleEnabled(), val -> config.setBatScaleEnabled(val))
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Float>createBuilder()
                                        .name(Component.literal("Scale size"))
                                        .description(OptionDescription.of(Component.literal("Scale multiplier (1.0 = normal size)")))
                                        .binding(defaults.getBatScaleSize(), () -> config.getBatScaleSize(), val -> config.setBatScaleSize(val))
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .range(1F, 10.0F)
                                                .step(0.1F))
                                        .build())

                                .build())

                        // === GROUPE HITBOX ===
                        .group(OptionGroup.createBuilder()
                                .name(Component.literal("Bat Hitbox"))
                                .description(OptionDescription.of(Component.literal("Hitbox bat related settings")))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.literal("Display hitbox"))
                                        .description(OptionDescription.of(Component.literal("Display the bat hitbox")))
                                        .binding(defaults.isBatHitboxEnabled(), () -> config.isBatHitboxEnabled(), val -> config.setBatHitboxEnabled(val))
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.literal("Rainbow hitbox"))
                                        .description(OptionDescription.of(Component.literal("Enable rainbow hitbox color")))
                                        .binding(defaults.isBatHitboxRainbow(), () -> config.isBatHitboxRainbow(), val -> config.setBatHitboxRainbow(val))
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Color>createBuilder()
                                        .name(Component.literal("Hitbox color"))
                                        .description(OptionDescription.of(Component.literal("Change the bat hitbox color if hitbox is enabled and rainbow display disabled")))
                                        .binding(defaults.getBatHitboxColor(), () -> config.getBatHitboxColor(), val -> config.setBatHitboxColor(val))
                                        .controller(ColorControllerBuilder::create)
                                        .build())

                                .option(Option.<Integer>createBuilder()
                                        .name(Component.literal("Rainbow speed"))
                                        .description(OptionDescription.of(Component.literal("Rainbow cycle in milliseconds")))
                                        .binding(defaults.getRainbowSpeed(), () -> config.getRainbowSpeed(), val -> config.setRainbowSpeed(val))
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(500, 10000)
                                                .step(100))
                                        .build())

                                .build())

                        .build())
                .save(ModConfig.HANDLER::save)
                .build()
                .generateScreen(parent);
    }

}