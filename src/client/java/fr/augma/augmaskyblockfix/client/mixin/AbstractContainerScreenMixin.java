package fr.augma.augmaskyblockfix.client.mixin;

import fr.augma.augmaskyblockfix.client.clipboard.ItemNbtCopier;
import fr.augma.augmaskyblockfix.client.config.MiscellaneousConfig;
import fr.augma.augmaskyblockfix.client.config.ModConfig;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {

    @Shadow
    protected Slot hoveredSlot;

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void copyHoveredItemNbt(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
        final MiscellaneousConfig miscellaneous = ModConfig.get().getMiscellaneous();
        if (!miscellaneous.isCopyItemNbt() || event.key() != miscellaneous.getCopyItemNbtKey()) {
            return;
        }
        if (this.hoveredSlot == null || !this.hoveredSlot.hasItem()) {
            return;
        }

        ItemNbtCopier.copy(this.hoveredSlot.getItem());
        cir.setReturnValue(true);
    }

}