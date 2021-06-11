package sirttas.elementalcraft.block.instrument.firefurnace.blast;

import net.minecraft.item.crafting.BlastingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.instrument.firefurnace.AbstractFireFurnaceBlockEntity;
import sirttas.elementalcraft.config.ECConfig;

public class FireBlastFurnaceBlockEntity extends AbstractFireFurnaceBlockEntity<BlastingRecipe> {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + FireBlastFurnaceBlock.NAME) public static final TileEntityType<FireBlastFurnaceBlockEntity> TYPE = null;


	public FireBlastFurnaceBlockEntity() {
		super(TYPE, IRecipeType.BLASTING, ECConfig.COMMON.fireBlastFurnaceTransferSpeed.get(), ECConfig.COMMON.fireBlastFurnaceMaxRunes.get());
	}
}
