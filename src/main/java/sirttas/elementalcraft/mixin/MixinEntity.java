package sirttas.elementalcraft.mixin;

import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sirttas.elementalcraft.jewel.JewelHelper;
import sirttas.elementalcraft.jewel.Jewels;

@Mixin(Entity.class)
public abstract class MixinEntity extends AttachmentHolder {

    @Inject(method = "canFreeze()Z",
            at = @At("RETURN"),
            cancellable = true)
    private void canFreeze$return(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ() && JewelHelper.hasJewel((Entity) (Object) this, Jewels.ARCTIC_HARES.get())) {
            cir.setReturnValue(true);
        }
    }
}
