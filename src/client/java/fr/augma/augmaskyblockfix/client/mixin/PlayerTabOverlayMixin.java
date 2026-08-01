package fr.augma.augmaskyblockfix.client.mixin;

import fr.augma.augmaskyblockfix.client.config.ModConfig;
import fr.augma.augmaskyblockfix.client.level.LevelRecolour;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerTabOverlay.class)
public abstract class PlayerTabOverlayMixin {

    @Inject(method = "getNameForDisplay", at = @At("RETURN"), cancellable = true)
    private void gradeLevelColour(PlayerInfo info, CallbackInfoReturnable<Component> cir) {
        final Component name = cir.getReturnValue();
        if (name == null || !ModConfig.get().getMiscellaneous().isLevelGradient()) {
            return;
        }

        cir.setReturnValue(LevelRecolour.apply(name));
    }

}
