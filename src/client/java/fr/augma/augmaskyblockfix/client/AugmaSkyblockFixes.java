package fr.augma.augmaskyblockfix.client;

import fr.augma.augmaskyblockfix.client.command.ConfigCommand;
import fr.augma.augmaskyblockfix.client.radial.RadialKeybind;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class AugmaSkyblockFixes implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Register client commands
        ClientCommandRegistrationCallback.EVENT.register(ConfigCommand::register);

        ClientTickEvents.END_CLIENT_TICK.register(RadialKeybind::tick);
    }

}