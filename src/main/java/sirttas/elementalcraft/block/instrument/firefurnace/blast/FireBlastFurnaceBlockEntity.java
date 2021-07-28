package sirttas.elementalcraft.block.instrument.firefurnace.blast;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.instrument.firefurnace.AbstractFireFurnaceBlockEntity;
import sirttas.elementalcraft.config.ECConfig;

public class FireBlastFurnaceBlockEntity extends AbstractFireFurnaceBlockEntity<BlastingRecipe> {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + FireBlastFurnaceBlock.NAME) public static final BlockEntityType<FireBlastFurnaceBlockEntity> TYPE = null;


	public FireBlastFurnaceBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state, RecipeType.BLASTING, ECConfig.COMMON.fireBlastFurnaceTransferSpeed.get(), ECConfig.COMMON.fireBlastFurnaceMaxRunes.get());
	}
}
