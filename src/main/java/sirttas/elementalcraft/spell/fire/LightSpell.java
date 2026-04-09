package sirttas.elementalcraft.spell.fire;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellCastResult;

import javax.annotation.Nonnull;

public class LightSpell extends Spell {

    public static final String NAME = "light";

    public LightSpell(ResourceKey<@NotNull Spell> key) {
        super(key);
    }

    @Nonnull
    @Override
    public SpellCastResult castOnBlock(@Nonnull Level level, @Nonnull Entity sender, @Nonnull BlockPos target, @Nonnull BlockHitResult hitResult) {
        var pos = target.relative(hitResult.getDirection());

        if (level.isClientSide() || !level.isEmptyBlock(pos)) {
            return SpellCastResult.PASS;
        }

        level.setBlockAndUpdate(pos, ECBlocks.ELEMENTAL_EMBER.get().defaultBlockState());
        return SpellCastResult.SUCCESS;
    }
}
