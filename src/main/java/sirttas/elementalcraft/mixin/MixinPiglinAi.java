package sirttas.elementalcraft.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sirttas.elementalcraft.jewel.JewelHelper;
import sirttas.elementalcraft.jewel.Jewels;

@Mixin(PiglinAi.class)
public abstract class MixinPiglinAi {

    @Inject(at = @At("RETURN"),
            method = "isWearingGold(Lnet/minecraft/world/entity/LivingEntity;)Z",
            cancellable = true)
    private static void isWearingGold$return(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ()) {
            cir.setReturnValue(JewelHelper.hasJewel(livingEntity, Jewels.PIGLIN.get()));
        }
    }
}
