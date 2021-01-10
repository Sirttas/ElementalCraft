package sirttas.elementalcraft.block.instrument.firefurnace;

import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.config.ECConfig;

public class TileFireFurnace extends AbstractTileFireFurnace<FurnaceRecipe> {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockFireFurnace.NAME) public static TileEntityType<TileFireFurnace> TYPE;

	public TileFireFurnace() {
		super(TYPE, IRecipeType.SMELTING, ECConfig.COMMON.fireFurnaceTransferSpeed.get(), ECConfig.COMMON.fireFurnaceMaxRunes.get());
	}

}
