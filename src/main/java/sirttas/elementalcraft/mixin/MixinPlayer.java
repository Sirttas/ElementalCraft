package sirttas.elementalcraft.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sirttas.elementalcraft.block.anchor.TranslocationAnchorListMessage;
import sirttas.elementalcraft.block.shrine.upgrade.translocation.TranslocationShrineUpgradeBlockItem;
import sirttas.elementalcraft.network.message.MessageHelper;
import sirttas.elementalcraft.spell.air.TranslocationSpell;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity implements IForgePlayer {

    protected MixinPlayer(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "tick()V",
            at = @At(value = "INVOKE", target="net.minecraft.world.item.ItemStack.isSameIgnoreDurability(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private void tick$invoke$isSameIgnoreDurability(CallbackInfo ci) {
        if (self() instanceof ServerPlayer serverPlayer && shouldSendAnchors(serverPlayer)) {
            MessageHelper.sendToPlayer(serverPlayer, TranslocationAnchorListMessage.create(serverPlayer.level));
        }
    }

    private static boolean shouldSendAnchors(ServerPlayer serverPlayer) {
        return TranslocationSpell.holdsTranslocation(serverPlayer) || TranslocationShrineUpgradeBlockItem.getTargetAnchor(serverPlayer) != null;
    }

    @Override
    @Unique
    public Player self() {
        return (Player) (Object) this;
    }
}
