package sirttas.elementalcraft.spell;

import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;

public interface IBlockCastedSpell {

	ActionResultType castOnBlock(Entity sender, BlockPos target);

}
