package sirttas.elementalcraft.interaction.mekanism.injector;

import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.common.recipe.MekanismRecipeType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;
import sirttas.elementalcraft.config.ECConfig;

public abstract class AbstractMekanismPureOreRecipeInjector<C extends IInventory, T extends IRecipe<C>> extends AbstractPureOreRecipeInjector<C, T> {

	protected AbstractMekanismPureOreRecipeInjector(IRecipeType<T> recipeType) {
		super(recipeType);
	}

	protected static ItemStack tweakOutput(ItemStack stack) {
		int count = stack.getCount();

		if (count > 2) {
			stack.setCount(getDimishedAmount(count));
		}
		if (count >= 4) {
			stack.setCount(getDimishedAmount(stack.getCount()));
		}
		if (count >= 8) {
			stack.setCount(getDimishedAmount(stack.getCount()));
		}
		return stack;
	}

	protected static <T extends Chemical<T>> ChemicalStack<T> tweakOutput(ChemicalStack<T> stack) {
		stack.setAmount(getDimishedAmount(stack.getAmount()));
		return stack;
	}

	protected static int getDimishedAmount(long count) {
		return (int) Math.round(count * ECConfig.COMMON.mekanismPureOreDimishingAmount.get());
	}

	@Override
	public ResourceLocation getRecipeTypeRegistryName() {
		IRecipeType<T> type = getRecipeType();

		if (type instanceof MekanismRecipeType) {
			return new ResourceLocation(((MekanismRecipeType<?>) type).toString());
		}
		return super.getRecipeTypeRegistryName();
	}
}
