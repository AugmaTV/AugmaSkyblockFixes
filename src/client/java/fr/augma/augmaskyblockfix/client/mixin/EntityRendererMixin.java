package fr.augma.augmaskyblockfix.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.augma.augmaskyblockfix.client.config.EntityEntryConfig;
import fr.augma.augmaskyblockfix.client.config.ModConfig;
import fr.augma.augmaskyblockfix.client.level.LevelRecolour;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityRenderer.class, priority = 2000)
public abstract class EntityRendererMixin {

    @Inject(method = "getNameTag", at = @At("RETURN"), cancellable = true)
    private void gradeLevelColour(Entity entity, CallbackInfoReturnable<Component> cir) {
        final Component name = cir.getReturnValue();
        if (name == null || !(entity instanceof Player) || !ModConfig.get().getMiscellaneous().isLevelGradientNametags()) {
            return;
        }

        cir.setReturnValue(LevelRecolour.apply(name));
    }

    @Inject(method = "submit", at = @At("TAIL"))
    private void renderEntityHitbox(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
        final EntityEntryConfig entry = ModConfig.get().getEntities().find(state.entityType);
        if (entry == null || !entry.isHitboxEnabled()) {
            return;
        }

        final AABB relativeBox = new AABB(-state.boundingBoxWidth / 2, 0, -state.boundingBoxWidth / 2, state.boundingBoxWidth / 2, state.boundingBoxHeight, state.boundingBoxWidth / 2);
        final int packedColor = entry.getHitboxRgb();
        final float lineWidth = Minecraft.getInstance().getWindow().getAppropriateLineWidth();
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.lines(), (pose, vertexConsumer) -> Shapes.create(relativeBox).forAllEdges((x1, y1, z1, x2, y2, z2) -> {
            final Vector3f normal = new Vector3f((float) (x2 - x1), (float) (y2 - y1), (float) (z2 - z1)).normalize();
            vertexConsumer.addVertex(pose, (float) x1, (float) y1, (float) z1).setColor(packedColor).setNormal(pose, normal).setLineWidth(lineWidth);
            vertexConsumer.addVertex(pose, (float) x2, (float) y2, (float) z2).setColor(packedColor).setNormal(pose, normal).setLineWidth(lineWidth);
        }));
    }

}