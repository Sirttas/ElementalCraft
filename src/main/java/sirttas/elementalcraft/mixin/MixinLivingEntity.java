package sirttas.elementalcraft.mixin;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.JewelHelper;
import sirttas.elementalcraft.jewel.StriderJewel;
import sirttas.elementalcraft.jewel.effect.DemigodJewel;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {

    protected MixinLivingEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "checkTotemDeathProtection(Lnet/minecraft/world/damagesource/DamageSource;)Z",
            at = @At("RETURN"),
            cancellable = true)
    private void checkTotemDeathProtection$return(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) || cir.getReturnValueZ()) {
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
        for (Jewel jewel :JewelHelper.getAllJewels(this)) {
            if (jewel instanceof StriderJewel striderJewel && striderJewel.canStandOnFluid(state, this)) {
                cir.setReturnValue(true);
                return;
            }
        }
    }

    @Inject(method = "getLiquidCollisionShape()Lnet/minecraft/world/phys/shapes/VoxelShape;",
            at = @At("RETURN"),
            cancellable = true)
    public void getLiquidCollisionShape$return(CallbackInfoReturnable<VoxelShape> cir) {
        if (!cir.getReturnValue().isEmpty()) {
            return;
        }
        for (Jewel jewel :JewelHelper.getAllJewels(this)) {
            if (jewel instanceof StriderJewel) {
                cir.setReturnValue(Shapes.block());
                return;
            }
        }
    }

}
