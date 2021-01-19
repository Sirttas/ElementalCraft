package sirttas.elementalcraft.block.instrument.infuser;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import sirttas.elementalcraft.block.instrument.IInstrument;
import sirttas.elementalcraft.recipe.instrument.infusion.AbstractInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.ToolInfusionRecipe;

public interface IInfuser extends IInstrument {

	default AbstractInfusionRecipe lookupInfusionRecipe(World world) {
		ToolInfusionRecipe toolInfusionRecipe = new ToolInfusionRecipe();

		return toolInfusionRecipe.matches(this) ? toolInfusionRecipe.with(this.getElementType()) : lookupRecipe(world, AbstractInfusionRecipe.TYPE);
	}

	default ItemStack getItem() {
		return this.getInventory().getStackInSlot(0);
	}
}
