package sirttas.elementalcraft.interaction.mekanism.recipe;

import mekanism.api.recipes.ItemStackToItemStackRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.recipe.instrument.io.grinding.AbstractGrindingRecipe;

public class MekanismCrusherRecipeWrapper extends AbstractGrindingRecipe {

	private final ItemStackToItemStackRecipe crushingRecipe;
	
	public MekanismCrusherRecipeWrapper(ItemStackToItemStackRecipe crushingRecipe) {
		this.crushingRecipe = crushingRecipe;
	}
	
	@Override
	public int getElementAmount() {
		return 1000;
	}

	@Override
	public boolean matches(ItemStack stack) {
		return crushingRecipe.getInput().test(stack);
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return crushingRecipe.getIngredients();
	}
	
	@Override
	public ItemStack getRecipeOutput() {
		return crushingRecipe.getOutput(ItemStack.EMPTY);
	}

	@Override
	public ResourceLocation getId() {
		return crushingRecipe.getId();
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return crushingRecipe.getSerializer();
	}

}
