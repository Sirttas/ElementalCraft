package sirttas.elementalcraft.spell.fire;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellCastResult;
import sirttas.elementalcraft.spell.properties.SpellProperties;

public class LightSpell extends Spell {

    public static final String NAME = "light";

    public LightSpell(Holder<SpellProperties> properties) {
        super(properties);
    }

    @Override
    public SpellCastResult castOnBlock(Level level, Entity sender, BlockPos target, BlockHitResult hitResult) {
        var pos = target.relative(hitResult.getDirection());

        if (level.isClientSide() || !level.isEmptyBlock(pos)) {
            return SpellCastResult.PASS;
        }

        level.setBlockAndUpdate(pos, ECBlocks.ELEMENTAL_EMBER.get().defaultBlockState());
        return SpellCastResult.SUCCESS;
    }
}
