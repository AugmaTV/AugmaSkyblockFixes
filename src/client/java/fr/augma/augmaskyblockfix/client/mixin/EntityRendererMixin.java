package fr.augma.augmaskyblockfix.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fr.augma.augmaskyblockfix.client.config.ModConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.Color;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void renderBatHitbox(EntityRenderState state, PoseStack poseStack, MultiBufferSource bufferSource, int light, CallbackInfo ci) {
        if (state.entityType != EntityType.BAT || !ModConfig.get().isBatHitboxEnabled()) {
            return;
        }

        final AABB relativeBox = new AABB(-state.boundingBoxWidth / 2, 0, -state.boundingBoxWidth / 2, state.boundingBoxWidth / 2, state.boundingBoxHeight, state.boundingBoxWidth / 2);
        final Color hitboxColor = ModConfig.get().getBatHitboxColor();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.lines());
        ShapeRenderer.renderLineBox(poseStack, vertexConsumer, relativeBox, hitboxColor.getRed() / 255.0F, hitboxColor.getGreen() / 255.0F, hitboxColor.getBlue() / 255.0F, 1.0F);
    }

}