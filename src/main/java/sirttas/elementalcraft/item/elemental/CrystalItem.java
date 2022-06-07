package sirttas.elementalcraft.item.elemental;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.entity.projectile.ThrownElementCrystal;

import javax.annotation.Nonnull;

public class CrystalItem extends ElementalItem {

    public CrystalItem(ElementType elementType) {
        super(elementType);
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!level.isClientSide) {
            var thrownStack = itemstack.copy();

            thrownStack.setCount(1);
            ThrownElementCrystal thrownElementCrystal = new ThrownElementCrystal(level, player);
            thrownElementCrystal.setItem(thrownStack);
            thrownElementCrystal.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(thrownElementCrystal);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}
