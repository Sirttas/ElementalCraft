package sirttas.elementalcraft.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sirttas.elementalcraft.block.anchor.TranslocationAnchorListPayload;
import sirttas.elementalcraft.block.shrine.upgrade.translocation.TranslocationShrineUpgradeBlockItem;
import sirttas.elementalcraft.network.payload.PayloadHelper;
import sirttas.elementalcraft.spell.air.TranslocationSpell;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity {

    protected MixinPlayer(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "tick()V",
            at = @At(value = "INVOKE", target="net.minecraft.world.item.ItemStack.isSameItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private void tick$invoke$isSameItem(CallbackInfo ci) {
        if (self() instanceof ServerPlayer serverPlayer && shouldSendAnchors(serverPlayer)) {
            PayloadHelper.sendToPlayer(serverPlayer, TranslocationAnchorListPayload.create(serverPlayer.level()));
        }
    }

    private static boolean shouldSendAnchors(ServerPlayer serverPlayer) {
        return TranslocationSpell.holdsTranslocation(serverPlayer) || TranslocationShrineUpgradeBlockItem.getTargetAnchor(serverPlayer) != null;
    }
}
