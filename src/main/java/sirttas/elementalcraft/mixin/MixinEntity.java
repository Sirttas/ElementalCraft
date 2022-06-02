package sirttas.elementalcraft.mixin;

import net.minecraft.world.Nameable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.extensions.IForgeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sirttas.elementalcraft.jewel.JewelHelper;
import sirttas.elementalcraft.jewel.Jewels;

@Mixin(Entity.class)
public abstract class MixinEntity extends CapabilityProvider<Entity> implements Nameable, EntityAccess, IForgeEntity {

    protected MixinEntity(Class<Entity> baseClass) {
        super(baseClass);
    }

    @Inject(method = "canFreeze()Z",
            at = @At("RETURN"),
            cancellable = true)
    private void canFreeze$return(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ() && JewelHelper.hasJewel((Entity) (Object) this, Jewels.ARCTIC_HARES.get())) {
            cir.setReturnValue(true);
        }
    }

}
