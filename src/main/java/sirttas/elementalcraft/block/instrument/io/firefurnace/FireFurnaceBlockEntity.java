package sirttas.elementalcraft.block.instrument.io.firefurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.config.ECConfig;

public class FireFurnaceBlockEntity extends AbstractFireFurnaceBlockEntity<SmeltingRecipe> {

	public FireFurnaceBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.FIRE_FURNACE, pos, state, RecipeType.SMELTING, ECConfig.COMMON.fireFurnaceTransferSpeed.get(), ECConfig.COMMON.fireFurnaceMaxRunes.get());
	}

}
