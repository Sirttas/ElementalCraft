package sirttas.elementalcraft.spell.repair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.phys.BlockHitResult;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellCastResult;
import sirttas.elementalcraft.spell.properties.SpellProperties;

public class RepairSpell extends Spell {

    public static final String NAME = "repair";

    public RepairSpell(Holder<SpellProperties> properties) {
        super(properties);
    }

    @Override
    public SpellCastResult castOnBlock(Level level, Entity sender, BlockPos target, BlockHitResult hitResult) {
        var offset = target.relative(hitResult.getDirection());

        if (!level.getBlockState(offset).isAir() || FallingBlock.isFree(level.getBlockState(offset.below()))) {
            return SpellCastResult.PASS;
        }

        if (sender instanceof Player player) {
            var item = getItemInOtherHand(player);

            if (!item.isEmpty() && item.isDamaged()) {
                repairPlayerItems(item);
                playSound(level, offset, player);
                return SpellCastResult.CHANNEL;
            }
        }

        return SpellCastResult.PASS;
    }

    private void repairPlayerItems(ItemStack stack) {
        stack.setDamageValue(stack.getDamageValue() - Math.min((int) (getStrength() * stack.getXpRepairRatio()), stack.getDamageValue()));
    }

    private static void playSound(Level level, BlockPos offset, Player player) {
        var useTicks = player.getUseItemRemainingTicks();

        if (useTicks > 0 && useTicks % 40 == 0) {
            level.levelEvent(player, LevelEvent.SOUND_ANVIL_USED, offset, 0);
        }
    }
}
