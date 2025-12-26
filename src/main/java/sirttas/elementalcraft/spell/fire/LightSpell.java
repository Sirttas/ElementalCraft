package sirttas.elementalcraft.spell.fire;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.spell.Spell;

import javax.annotation.Nonnull;

public class LightSpell extends Spell {

    public static final String NAME = "light";

    public LightSpell(ResourceKey<Spell> key) {
        super(key);
    }

    @Nonnull
    @Override
    public InteractionResult castOnBlock(@Nonnull Level level, @Nonnull Entity sender, @Nonnull BlockPos target, @Nonnull BlockHitResult hitResult) {
        var pos = target.relative(hitResult.getDirection());

        if (level.isClientSide || !level.isEmptyBlock(pos)) {
            return InteractionResult.PASS;
        }

        level.setBlockAndUpdate(pos, ECBlocks.ELEMENTAL_EMBER.get().defaultBlockState());
        return InteractionResult.SUCCESS;
    }
}
