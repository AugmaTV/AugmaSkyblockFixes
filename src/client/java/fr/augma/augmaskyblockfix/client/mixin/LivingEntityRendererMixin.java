package fr.augma.augmaskyblockfix.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.augma.augmaskyblockfix.client.config.ModConfig;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

    @Inject(method = "scale", at = @At("HEAD"))
    private void scale(LivingEntityRenderState renderState, PoseStack poseStack, CallbackInfo ci) {
        if (renderState.entityType == EntityType.BAT && ModConfig.get().isBatScaleEnabled()) {
            final float scale = ModConfig.get().getBatScaleSize();
            final float yOffset = renderState.boundingBoxHeight * (scale - 1.0F) * 0.5F;
            poseStack.translate(0.0F, yOffset, 0.0F);
            poseStack.scale(scale, scale, scale);
        }
    }

}