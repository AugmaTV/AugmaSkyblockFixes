package fr.augma.augmaskyblockfix.client.mixin;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin<T extends AbstractContainerMenu> extends Screen implements MenuAccess<T> {

    @Shadow
    protected T menu;

    protected AbstractContainerScreenMixin(Component component) {
        super(component);
    }

    @Inject(method = "removed", at = @At("HEAD"))
    public void removed(CallbackInfo ci) {
        if (menu instanceof InventoryMenu) {
            final InventoryMenu inventoryMenu = (InventoryMenu) menu;
            if (inventoryMenu.getCarried() != ItemStack.EMPTY && inventoryMenu.getCarried().getItem() == Items.NETHER_STAR) {
                final ItemStack itemStack = inventoryMenu.getCarried();
                final Optional<String> optSkyblockId = itemStack.getComponents().get(DataComponents.CUSTOM_DATA).copyTag().getString("id");
                if (optSkyblockId.isPresent() && optSkyblockId.get().equalsIgnoreCase("SKYBLOCK_MENU")) {
                    inventoryMenu.setCarried(ItemStack.EMPTY);
                }
            }
        }
    }

}