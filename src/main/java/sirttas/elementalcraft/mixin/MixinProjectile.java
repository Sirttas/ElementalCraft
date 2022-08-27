package sirttas.elementalcraft.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.jewel.JewelHelper;
import sirttas.elementalcraft.jewel.Jewels;

@Mixin(Projectile.class)
public abstract class MixinProjectile extends Entity {

    @Unique
    private boolean homing = false;
    @Unique
    private double maxReachedSpeed = -1;

    @Shadow
    private boolean hasBeenShot;
    @Shadow
    public abstract Entity getOwner();

    protected MixinProjectile(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "tick()V",
            at = @At("HEAD"))
    private void tick$head(CallbackInfo ci) {
        this.handleHoming();
    }

    private void handleHoming() {
        //noinspection ConstantConditions
        if ((Entity) this instanceof AbstractArrow arrow && arrow.inGround) {
            return;
        }

        var owner = this.getOwner();
        if (owner == null) {
            return;
        }

        var hawk = Jewels.HAWK.get();

        if (!hasBeenShot && JewelHelper.hasJewel(owner, hawk)) {
            homing = true;
            if (!this.level.isClientSide) {
                hawk.consume(owner);
            }
            return;
        }
        if (!homing) {
            return;
        }

        var hit = EntityHelper.rayTrace(owner, 100);
        if (hit.getType() == HitResult.Type.MISS) {
            return;
        }

        setHomingTo(hit.getLocation());
    }

    private void setHomingTo(Vec3 target) {
        var oldDelta = this.getDeltaMovement();
        var length = oldDelta.length();
        var targetVector = target.subtract(this.position()).normalize();
        var normalizedDelta = oldDelta.normalize();

        maxReachedSpeed = Math.max(maxReachedSpeed, length);
        if (Math.acos(normalizedDelta.dot(targetVector)) > Math.PI / 16) {
            length *= 0.995;
        } else {
            length = Math.min(length * 1.005, maxReachedSpeed);
        }

        this.setDeltaMovement(targetVector.add(normalizedDelta).normalize().scale(length));
    }
}
