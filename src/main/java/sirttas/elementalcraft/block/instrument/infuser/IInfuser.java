package sirttas.elementalcraft.block.instrument.infuser;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.block.instrument.IInstrument;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;

public interface IInfuser extends IInstrument {

	default IInfusionRecipe lookupInfusionRecipe(Level world) {
		return lookupRecipe(world, IInfusionRecipe.TYPE);
	}

	default ItemStack getItem() {
		return this.getInventory().getItem(0);
	}
}
