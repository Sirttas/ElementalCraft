package sirttas.elementalcraft.block.instrument.firefurnace.blast;

import net.minecraft.item.crafting.BlastingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.firefurnace.AbstractTileFireFurnace;

public class TileFireBlastFurnace extends AbstractTileFireFurnace<BlastingRecipe> {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockFireBlastFurnace.NAME) public static TileEntityType<TileFireBlastFurnace> TYPE;


	public TileFireBlastFurnace() {
		super(TYPE, IRecipeType.BLASTING);
	}
}
