package sirttas.elementalcraft.interaction.mekanism.recipe;

import mekanism.api.recipes.ItemStackToItemStackRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

import javax.annotation.Nonnull;

public class MekanismCrusherRecipeWrapper implements IGrindingRecipe {

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
		return crushingRecipe.getInput().test(stack) && IGrindingRecipe.super.matches(stack);
	}
	
	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return crushingRecipe.getIngredients();
	}
	
	@Nonnull
	@Override
	public ItemStack getResultItem() {
		return crushingRecipe.getOutput(ItemStack.EMPTY);
	}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return crushingRecipe.getId();
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return crushingRecipe.getSerializer();
	}

}
