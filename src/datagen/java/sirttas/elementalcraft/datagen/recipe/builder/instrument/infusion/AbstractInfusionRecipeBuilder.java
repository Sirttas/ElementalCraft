package sirttas.elementalcraft.datagen.recipe.builder.instrument.infusion;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;

public abstract class AbstractInfusionRecipeBuilder {

	protected final Ingredient ingredient;
	protected int elementAmount;

	protected AbstractInfusionRecipeBuilder(Ingredient ingredient) {
		this.ingredient = ingredient;
		elementAmount = 1000;
	}

	public AbstractInfusionRecipeBuilder withElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
		return this;
	}

	protected abstract ResourceLocation getId();
	
	public void save(RecipeOutput recipeOutput) {
		ResourceLocation id = getId();

		this.save(recipeOutput, new ResourceLocation(id.getNamespace(), IInfusionRecipe.NAME + '/' + id.getPath()));
	}

	public void save(RecipeOutput recipeOutput, String save) {
		ResourceLocation resourcelocation = getId();
		if ((new ResourceLocation(save)).equals(resourcelocation)) {
			throw new IllegalStateException("Infusion Recipe " + save + " should remove its 'save' argument");
		} else {
			this.save(recipeOutput, ElementalCraftApi.createRL(IInfusionRecipe.NAME + '/' + save));
		}
	}

	public abstract void save(RecipeOutput recipeOutput, ResourceLocation id);
}
