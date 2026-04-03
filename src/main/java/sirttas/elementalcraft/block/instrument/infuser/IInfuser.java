package sirttas.elementalcraft.block.instrument.infuser;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.IInstrument;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.input.SingleItemSingleElementRecipeInput;
import sirttas.elementalcraft.recipe.instrument.infusion.InfusionRecipe;

public interface IInfuser extends IInstrument {

	default InfusionRecipe lookupInfusionRecipe(Level level) {
		if (getContainerElementType() == ElementType.NONE) {
			return null;
		}
		return lookupRecipe(level, ECRecipeTypes.INFUSION.get(), createInfusionRecipeInput());
	}

	default @NotNull SingleItemSingleElementRecipeInput createInfusionRecipeInput() {
		var container = getContainer();

		return new SingleItemSingleElementRecipeInput(
				getItem(),
				container.getElementType(),
				container.getElementAmount());
	}

	default ItemStack getItem() {
		return this.getInventory().getItem(0);
	}
}
