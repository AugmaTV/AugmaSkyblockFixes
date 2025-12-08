package fr.augma.augmaskyblockfix.client;

import fr.augma.augmaskyblockfix.client.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;

public class AugmaSkyblockFixes implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModConfig.HANDLER.load();
    }

}