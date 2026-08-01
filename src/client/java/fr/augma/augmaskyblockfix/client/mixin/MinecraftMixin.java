package fr.augma.augmaskyblockfix.client.mixin;

import fr.augma.augmaskyblockfix.client.radial.RadialOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Inject(method = "setScreenAndShow", at = @At("HEAD"))
    private void closeRadialOverlay(Screen screen, CallbackInfo ci) {
        if (screen != null) {
            RadialOverlay.close(false);
        }
    }

}