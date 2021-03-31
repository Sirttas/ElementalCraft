package sirttas.elementalcraft.block.instrument.infuser;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import sirttas.elementalcraft.block.instrument.IInstrument;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;

public interface IInfuser extends IInstrument {

	default IInfusionRecipe lookupInfusionRecipe(World world) {
		return lookupRecipe(world, IInfusionRecipe.TYPE);
	}

	default ItemStack getItem() {
		return this.getInventory().getStackInSlot(0);
	}
}
