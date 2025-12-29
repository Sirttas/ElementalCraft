package sirttas.elementalcraft.spell.repair;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.phys.BlockHitResult;
import sirttas.elementalcraft.spell.Spell;

import javax.annotation.Nonnull;

public class RepairSpell extends Spell {

    public static final String NAME = "repair";

    public RepairSpell(ResourceKey<Spell> key) {
        super(key);
    }

    @Nonnull
    @Override
    public InteractionResult castOnBlock(@Nonnull Level level, @Nonnull Entity sender, @Nonnull BlockPos target, @Nonnull BlockHitResult hitResult) {
        var offset = target.relative(hitResult.getDirection());

        if (!level.getBlockState(offset).isAir() || FallingBlock.isFree(level.getBlockState(offset.below()))) {
            return InteractionResult.PASS;
        }

        if (sender instanceof Player player) {
            var item = getItemInOtherHand(player);

            if (!item.isEmpty() && item.isDamaged()) {
                repairPlayerItems(item);
                playSound(level, offset, player);
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }

    private void repairPlayerItems(ItemStack stack) {
        stack.setDamageValue(stack.getDamageValue() - Math.min((int) (getStrength() * stack.getXpRepairRatio()), stack.getDamageValue()));
    }

    private static void playSound(@Nonnull Level level, BlockPos offset, Player player) {
        var useTicks = player.getUseItemRemainingTicks();

        if (useTicks > 0 && useTicks % 40 == 0) {
            level.levelEvent(player, LevelEvent.SOUND_ANVIL_USED, offset, 0);
        }
    }
}
