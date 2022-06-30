package sirttas.elementalcraft.block.instrument.io.firefurnace.blast;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.io.firefurnace.AbstractFireFurnaceBlockEntity;
import sirttas.elementalcraft.config.ECConfig;

public class FireBlastFurnaceBlockEntity extends AbstractFireFurnaceBlockEntity<BlastingRecipe> {


	public FireBlastFurnaceBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.FIRE_BLAST_FURNACE, pos, state, RecipeType.BLASTING, ECConfig.COMMON.fireBlastFurnaceTransferSpeed.get(), ECConfig.COMMON.fireBlastFurnaceMaxRunes.get());
	}
}
