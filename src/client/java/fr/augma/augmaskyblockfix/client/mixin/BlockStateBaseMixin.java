package fr.augma.augmaskyblockfix.client.mixin;

import fr.augma.augmaskyblockfix.client.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {

    @Inject(method = "getOffset", at = @At("HEAD"), cancellable = true)
    private void centerPlants(BlockPos pos, CallbackInfoReturnable<Vec3> cir) {
        if (Minecraft.getInstance() != null && ModConfig.get().getMiscellaneous().isCenteredPlants()) {
            cir.setReturnValue(Vec3.ZERO);
        }
    }

}