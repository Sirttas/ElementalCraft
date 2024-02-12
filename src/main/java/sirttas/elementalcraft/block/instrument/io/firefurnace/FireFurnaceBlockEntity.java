package sirttas.elementalcraft.block.instrument.io.firefurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.instrument.io.FurnaceRecipeWrapper;

public class FireFurnaceBlockEntity extends AbstractFireFurnaceBlockEntity<SmeltingRecipe> {

	private static final Config<AbstractFireFurnaceBlockEntity<SmeltingRecipe>, FurnaceRecipeWrapper<SmeltingRecipe>> CONFIG = new Config<>(
			ECBlockEntityTypes.FIRE_FURNACE,
			null,
			ECConfig.COMMON.fireFurnaceTransferSpeed,
			ECConfig.COMMON.fireFurnaceMaxRunes,
			1,
			false,
			false
	);

	public FireFurnaceBlockEntity(BlockPos pos, BlockState state) {
		super(CONFIG, RecipeType.SMELTING, pos, state);
	}

}
