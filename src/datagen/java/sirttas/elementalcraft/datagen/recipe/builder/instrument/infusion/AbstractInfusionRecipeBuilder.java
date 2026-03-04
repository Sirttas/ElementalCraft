package sirttas.elementalcraft.datagen.recipe.builder.instrument.infusion;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
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

	protected abstract Identifier getId();
	
	public void save(RecipeOutput recipeOutput) {
		Identifier id = getId();

		this.save(recipeOutput, Identifier.fromNamespaceAndPath(id.getNamespace(), IInfusionRecipe.NAME + '/' + id.getPath()));
	}

	public void save(RecipeOutput recipeOutput, String save) {
		Identifier Identifier = getId();
		if (Identifier.parse(save).equals(Identifier)) {
			throw new IllegalStateException("Infusion Recipe " + save + " should remove its 'save' argument");
		} else {
			this.save(recipeOutput, ElementalCraftApi.createRL(IInfusionRecipe.NAME + '/' + save));
		}
	}

	public abstract void save(RecipeOutput recipeOutput, Identifier id);
}
