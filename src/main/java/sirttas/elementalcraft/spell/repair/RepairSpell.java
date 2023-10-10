package sirttas.elementalcraft.spell.repair;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.phys.BlockHitResult;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;

import javax.annotation.Nonnull;

public class RepairSpell extends Spell {

    public static final String NAME = "repair";

    public RepairSpell(ResourceKey<Spell> key) {
        super(key);
    }

    @Nonnull
    @Override
    public InteractionResult castOnBlock(@Nonnull Entity sender, @Nonnull BlockPos target, @Nonnull BlockHitResult hitResult) {
        var offset = target.relative(hitResult.getDirection());

        if (!sender.level().getBlockState(offset).isAir() || FallingBlock.isFree(sender.level().getBlockState(offset.below()))) {
            return InteractionResult.PASS;
        }

        if (sender instanceof Player player) {
            var item = getItemToRepair(player);

            if (!item.isEmpty() && item.isDamaged()) {
                repairPlayerItems(item);
                playSound(offset, player);
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }

    public ItemStack getItemToRepair(Player player) {
        var mainHand = player.getMainHandItem();
        var offHand = player.getOffhandItem();

        if (SpellHelper.getSpell(mainHand) == this) {
            return offHand;
        } else if (SpellHelper.getSpell(offHand) == this) {
            return mainHand;
        } else {
            return ItemStack.EMPTY;
        }
    }

    private void repairPlayerItems(ItemStack stack) {
        stack.setDamageValue(stack.getDamageValue() - Math.min((int) (1 /* TODO spell strength property */ * stack.getXpRepairRatio()), stack.getDamageValue()));
    }

    private static void playSound(BlockPos offset, Player player) {
        var useTicks = player.getUseItemRemainingTicks();

        if (useTicks > 0 && useTicks % 40 == 0) {
            player.level().levelEvent(player, LevelEvent.SOUND_ANVIL_USED, offset, 0);
        }
    }
}
