package sirttas.elementalcraft.block.instrument.io.firefurnace.blast;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.io.firefurnace.AbstractFireFurnaceBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.instrument.io.FurnaceRecipeWrapper;

public class FireBlastFurnaceBlockEntity extends AbstractFireFurnaceBlockEntity<BlastingRecipe> {

	private static final Config<AbstractFireFurnaceBlockEntity<BlastingRecipe>, FurnaceRecipeWrapper<BlastingRecipe>> CONFIG = new Config<>(
			ECBlockEntityTypes.FIRE_BLAST_FURNACE,
			null,
			ECConfig.COMMON.fireBlastFurnaceTransferSpeed,
			ECConfig.COMMON.fireBlastFurnaceMaxRunes,
			1,
			false,
			false
	);

	public FireBlastFurnaceBlockEntity(BlockPos pos, BlockState state) {
		super(CONFIG, RecipeType.BLASTING, pos, state);
	}
}
