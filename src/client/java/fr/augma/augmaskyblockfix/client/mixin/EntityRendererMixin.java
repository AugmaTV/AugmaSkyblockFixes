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
        if (state.entityType == EntityType.BAT && ModConfig.get().batHitboxEnabled) {
            AABB relativeBox = new AABB(
                -state.boundingBoxWidth / 2, 0, -state.boundingBoxWidth / 2,
                    state.boundingBoxWidth / 2, state.boundingBoxHeight, state.boundingBoxWidth / 2
            );

            float r;
            float g;
            float b;
            if (ModConfig.get().batHitboxRainbow) {
                final int rainbowSpeed = ModConfig.get().rainbowSpeed;
                final float hue = (System.currentTimeMillis() % rainbowSpeed) / (float) rainbowSpeed;
                final float[] rgb = getRainbowColor(hue);
                r = rgb[0];
                g = rgb[1];
                b = rgb[2];
            } else {
                Color color = ModConfig.get().batHitboxColor;
                r = color.getRed() / 255.0F;
                g = color.getGreen() / 255.0F;
                b = color.getBlue() / 255.0F;
            }

            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.lines());
            ShapeRenderer.renderLineBox(poseStack, vertexConsumer, relativeBox, r, g, b, 1.0F);
        }
    }

    @Unique
    private static float[] getRainbowColor(float hue) {
        float r, g, b;
        int i = (int) (hue * 6) % 6;
        float f = hue * 6 - i;
        float q = 1 - f;

        switch (i) {
            case 0 -> { r = 1; g = f; b = 0; }
            case 1 -> { r = q; g = 1; b = 0; }
            case 2 -> { r = 0; g = 1; b = f; }
            case 3 -> { r = 0; g = q; b = 1; }
            case 4 -> { r = f; g = 0; b = 1; }
            default -> { r = 1; g = 0; b = q; }
        }

        return new float[] { r, g, b };
    }
}
