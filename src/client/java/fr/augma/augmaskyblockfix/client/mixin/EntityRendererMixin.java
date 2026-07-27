package fr.augma.augmaskyblockfix.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.augma.augmaskyblockfix.client.config.ModConfig;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {

    @Inject(method = "submit", at = @At("TAIL"))
    private void renderBatHitbox(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
        if (state.entityType != EntityTypes.BAT || !ModConfig.get().getDungeon().getBat().isHitboxEnabled()) {
            return;
        }

        final AABB relativeBox = new AABB(-state.boundingBoxWidth / 2, 0, -state.boundingBoxWidth / 2, state.boundingBoxWidth / 2, state.boundingBoxHeight, state.boundingBoxWidth / 2);
        final int packedColor = ModConfig.get().getDungeon().getBat().getHitboxRgb();
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.lines(), (pose, vertexConsumer) -> Shapes.create(relativeBox).forAllEdges((x1, y1, z1, x2, y2, z2) -> {
            final Vector3f normal = new Vector3f((float) (x2 - x1), (float) (y2 - y1), (float) (z2 - z1)).normalize();
            vertexConsumer.addVertex(pose, (float) x1, (float) y1, (float) z1).setColor(packedColor).setNormal(pose, normal).setLineWidth(2.5F);
            vertexConsumer.addVertex(pose, (float) x2, (float) y2, (float) z2).setColor(packedColor).setNormal(pose, normal).setLineWidth(2.5F);
        }));
    }

}