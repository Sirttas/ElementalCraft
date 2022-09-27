package sirttas.elementalcraft.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sirttas.elementalcraft.tag.ECTags;

@Mixin(Zombie.class)
public abstract class MixinZombie extends Monster {

    protected MixinZombie(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "wantsToPickUp(Lnet/minecraft/world/item/ItemStack;)Z",
            at = @At("HEAD"),
            cancellable = true)
    public void wantsToPickUp$head(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.is(ECTags.Items.SHARDS)) {
            cir.setReturnValue(false);
        }
    }
}
