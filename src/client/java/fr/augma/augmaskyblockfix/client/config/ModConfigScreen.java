package fr.augma.augmaskyblockfix.client.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.Color;

public class ModConfigScreen {

    public static Screen create(Screen parent) {
        ModConfig config = ModConfig.get();
        ModConfig defaults = new ModConfig();

        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("AugmaSkyblockFixes Configuration"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("Bat Settings"))
                        .tooltip(Component.literal("Configuration des chauves-souris"))

                        // === GROUPE SCALE ===
                        .group(OptionGroup.createBuilder()
                                .name(Component.literal("Scale"))
                                .description(OptionDescription.of(Component.literal("Options de mise à l'échelle des chauves-souris")))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.literal("Activer le Scale"))
                                        .description(OptionDescription.of(Component.literal("Active ou désactive l'agrandissement des chauves-souris")))
                                        .binding(defaults.batScaleEnabled, () -> config.batScaleEnabled, val -> config.batScaleEnabled = val)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Float>createBuilder()
                                        .name(Component.literal("Taille du Scale"))
                                        .description(OptionDescription.of(Component.literal("Multiplicateur de taille des chauves-souris (1.0 = taille normale)")))
                                        .binding(defaults.batScaleSize, () -> config.batScaleSize, val -> config.batScaleSize = val)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .range(0.5F, 10.0F)
                                                .step(0.5F))
                                        .build())

                                .build())

                        // === GROUPE HITBOX ===
                        .group(OptionGroup.createBuilder()
                                .name(Component.literal("Hitbox"))
                                .description(OptionDescription.of(Component.literal("Options d'affichage de la hitbox des chauves-souris")))

                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.literal("Afficher la Hitbox"))
                                        .description(OptionDescription.of(Component.literal("Affiche une hitbox autour des chauves-souris")))
                                        .binding(defaults.batHitboxEnabled, () -> config.batHitboxEnabled, val -> config.batHitboxEnabled = val)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.literal("Mode Rainbow"))
                                        .description(OptionDescription.of(Component.literal("Active le mode arc-en-ciel pour la hitbox")))
                                        .binding(defaults.batHitboxRainbow, () -> config.batHitboxRainbow, val -> config.batHitboxRainbow = val)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Color>createBuilder()
                                        .name(Component.literal("Couleur de la Hitbox"))
                                        .description(OptionDescription.of(Component.literal("Couleur de la hitbox (utilisée si le mode rainbow est désactivé)")))
                                        .binding(defaults.batHitboxColor, () -> config.batHitboxColor, val -> config.batHitboxColor = val)
                                        .controller(ColorControllerBuilder::create)
                                        .build())

                                .option(Option.<Integer>createBuilder()
                                        .name(Component.literal("Vitesse Rainbow"))
                                        .description(OptionDescription.of(Component.literal("Durée d'un cycle rainbow en millisecondes")))
                                        .binding(defaults.rainbowSpeed, () -> config.rainbowSpeed, val -> config.rainbowSpeed = val)
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