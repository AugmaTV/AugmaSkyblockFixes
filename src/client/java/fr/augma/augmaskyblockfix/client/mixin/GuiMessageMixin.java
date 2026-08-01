package fr.augma.augmaskyblockfix.client.mixin;

import fr.augma.augmaskyblockfix.client.config.ModConfig;
import fr.augma.augmaskyblockfix.client.level.LevelRecolour;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GuiMessage.class)
public abstract class GuiMessageMixin {

    @ModifyArg(method = "splitLines(Lnet/minecraft/client/gui/Font;I)Ljava/util/List;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ComponentRenderUtils;wrapComponents(Lnet/minecraft/network/chat/FormattedText;ILnet/minecraft/client/gui/Font;)Ljava/util/List;"), index = 0)
    private FormattedText gradeLevelColour(FormattedText text) {
        if (!(text instanceof Component component) || !ModConfig.get().getMiscellaneous().isLevelGradientChat()) {
            return text;
        }

        return LevelRecolour.apply(component);
    }

}
