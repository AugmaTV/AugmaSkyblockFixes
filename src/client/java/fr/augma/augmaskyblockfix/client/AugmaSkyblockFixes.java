package fr.augma.augmaskyblockfix.client;

import fr.augma.augmaskyblockfix.client.command.ConfigCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class AugmaSkyblockFixes implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Register client commands
        ClientCommandRegistrationCallback.EVENT.register(ConfigCommand::register);
    }

}