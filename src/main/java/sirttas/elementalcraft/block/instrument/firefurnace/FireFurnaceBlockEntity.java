package sirttas.elementalcraft.block.instrument.firefurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.config.ECConfig;

public class FireFurnaceBlockEntity extends AbstractFireFurnaceBlockEntity<SmeltingRecipe> {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + FireFurnaceBlock.NAME) public static final BlockEntityType<FireFurnaceBlockEntity> TYPE = null;

	public FireFurnaceBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state, RecipeType.SMELTING, ECConfig.COMMON.fireFurnaceTransferSpeed.get(), ECConfig.COMMON.fireFurnaceMaxRunes.get());
	}

}
