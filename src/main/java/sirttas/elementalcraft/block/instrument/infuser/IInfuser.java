package sirttas.elementalcraft.block.instrument.infuser;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.IInstrument;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;

public interface IInfuser extends IInstrument {

	default IInfusionRecipe lookupInfusionRecipe(Level level) {
		if (getContainerElementType() == ElementType.NONE) {
			return null;
		}
		return lookupRecipe(level, ECRecipeTypes.INFUSION.get());
	}

	default ItemStack getItem() {
		return this.getInventory().getItem(0);
	}
}
