package fr.augma.augmaskyblockfix.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import fr.augma.augmaskyblockfix.client.config.ModConfig;
import fr.augma.augmaskyblockfix.client.level.LevelRecolour;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerTabOverlay.class)
public abstract class PlayerTabOverlayMixin {

    @ModifyExpressionValue(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;ILnet/minecraft/world/scores/Scoreboard;Lnet/minecraft/world/scores/Objective;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;getNameForDisplay(Lnet/minecraft/client/multiplayer/PlayerInfo;)Lnet/minecraft/network/chat/Component;"))
    private Component gradeLevelColour(Component name) {
        if (name == null || !ModConfig.get().getMiscellaneous().isLevelGradientTab()) {
            return name;
        }

        return LevelRecolour.apply(name);
    }

}
