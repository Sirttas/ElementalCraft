package sirttas.elementalcraft.block.instrument.firefurnace;

import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.config.ECConfig;

public class FireFurnaceBlockEntity extends AbstractFireFurnaceBlockEntity<FurnaceRecipe> {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + FireFurnaceBlock.NAME) public static final TileEntityType<FireFurnaceBlockEntity> TYPE = null;

	public FireFurnaceBlockEntity() {
		super(TYPE, IRecipeType.SMELTING, ECConfig.COMMON.fireFurnaceTransferSpeed.get(), ECConfig.COMMON.fireFurnaceMaxRunes.get());
	}

}
