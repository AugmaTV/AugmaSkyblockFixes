package fr.augma.augmaskyblockfix.client.mixin;

import fr.augma.augmaskyblockfix.client.config.ModConfig;
import fr.augma.augmaskyblockfix.client.radial.RadialOverlay;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {

    @Inject(method = "onButton", at = @At("HEAD"), cancellable = true)
    private void handleRadialClick(long window, MouseButtonInfo info, int action, CallbackInfo ci) {
        if (!RadialOverlay.isOpen()) {
            return;
        }

        final boolean onRelease = ModConfig.get().getRadial().isActivateOnRelease() && !RadialOverlay.isCenterActive();
        if (action == GLFW.GLFW_PRESS) {
            if (info.button() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                RadialOverlay.back();
            } else if (!onRelease) {
                RadialOverlay.activate();
            }
        } else if (action == GLFW.GLFW_RELEASE && onRelease && info.button() != GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            RadialOverlay.activate();
        }
        ci.cancel();
    }

}