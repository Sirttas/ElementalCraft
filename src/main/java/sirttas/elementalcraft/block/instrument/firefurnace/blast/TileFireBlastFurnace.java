package sirttas.elementalcraft.block.instrument.firefurnace.blast;

import net.minecraft.item.crafting.BlastingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.firefurnace.AbstractTileFireFurnace;
import sirttas.elementalcraft.config.ECConfig;

public class TileFireBlastFurnace extends AbstractTileFireFurnace<BlastingRecipe> {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockFireBlastFurnace.NAME) public static final TileEntityType<TileFireBlastFurnace> TYPE = null;


	public TileFireBlastFurnace() {
		super(TYPE, IRecipeType.BLASTING, ECConfig.COMMON.fireBlastFurnaceTransferSpeed.get(), ECConfig.COMMON.fireBlastFurnaceMaxRunes.get());
	}
}
