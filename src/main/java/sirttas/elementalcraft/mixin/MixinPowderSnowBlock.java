package sirttas.elementalcraft.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sirttas.elementalcraft.jewel.JewelHelper;
import sirttas.elementalcraft.jewel.Jewels;

@Mixin(PowderSnowBlock.class)
public abstract class MixinPowderSnowBlock extends Block implements BucketPickup  {

    protected MixinPowderSnowBlock(Properties properties) {
        super(properties);
    }

    @Inject(method = "canEntityWalkOnPowderSnow(Lnet/minecraft/world/entity/Entity;)Z",
            at = @At("RETURN"),
            cancellable = true)
    private static void canEntityWalkOnPowderSnow$return(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ() && JewelHelper.hasJewel(entity, Jewels.ARCTIC_HARES)) {
            cir.setReturnValue(true);
        }
    }
}
