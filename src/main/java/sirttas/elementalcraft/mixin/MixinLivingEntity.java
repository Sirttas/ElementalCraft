package sirttas.elementalcraft.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sirttas.elementalcraft.jewel.DemigodJewel;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.JewelHelper;
import sirttas.elementalcraft.jewel.StriderJewel;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {

    protected MixinLivingEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "checkTotemDeathProtection(Lnet/minecraft/world/damagesource/DamageSource;)Z",
            at = @At("RETURN"),
            cancellable = true)
    private void checkTotemDeathProtection$return(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (damageSource.isBypassInvul() || cir.getReturnValueZ()) {
            return;
        }
        if (DemigodJewel.trigger((LivingEntity) (Object) this)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "canStandOnFluid(Lnet/minecraft/world/level/material/FluidState;)Z",
            at = @At("RETURN"),
            cancellable = true)
    public void canStandOnFluid$return(FluidState state, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) {
            return;
        }
        for (Jewel jewel :JewelHelper.getActiveJewels(this)) {
            if (jewel instanceof StriderJewel striderJewel && state.is(striderJewel.getTag())) {
                cir.setReturnValue(true);
                return;
            }
        }
    }

}
