package fr.augma.augmaskyblockfix.client.command;

import com.mojang.brigadier.CommandDispatcher;
import fr.augma.augmaskyblockfix.client.config.ModConfigScreen;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;

public class ConfigCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext registryAccess) {
        dispatcher.register(
            ClientCommandManager.literal("asfconfig")
                .executes(context -> {
                    // Schedule opening the screen on the next tick to avoid issues
                    Minecraft.getInstance().schedule(() -> {
                        Minecraft.getInstance().setScreen(ModConfigScreen.create(null));
                    });
                    return 1;
                })
        );

        // Also register /augmaskyblockfixes as an alias
        dispatcher.register(
            ClientCommandManager.literal("augmaskyblockfixes")
                .executes(context -> {
                    Minecraft.getInstance().schedule(() -> {
                        Minecraft.getInstance().setScreen(ModConfigScreen.create(null));
                    });
                    return 1;
                })
        );
    }
}
