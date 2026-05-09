package sirttas.elementalcraft.datagen.recipe.builder.instrument;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.recipe.instrument.io.sawing.SawingRecipe;

public class SawingRecipeBuilder {

	private final Item result;
	private Ingredient ingredient;
	private int elementAmount;
	private double luckRatio;
	private int count;

	public SawingRecipeBuilder(ItemLike result) {
		this.result = result.asItem();
		elementAmount = 1000;
		luckRatio = 0;
		count = 1;
	}

	public static SawingRecipeBuilder sawingRecipe(ItemLike result) {
		return new SawingRecipeBuilder(result);
	}
	
	public SawingRecipeBuilder withElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
		return this;
	}

	public SawingRecipeBuilder withIngredient(ItemLike item) {
		return this.withIngredient(Ingredient.of(item));
	}
	
	public SawingRecipeBuilder withIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
		return this;
	}

	public SawingRecipeBuilder withLuckRatio(double luckRatio) {
		this.luckRatio = luckRatio;
		return this;
	}

	public SawingRecipeBuilder withCount(int count) {
		this.count = count;
		return this;
	}

	public void save(RecipeOutput recipeOutput) {
		Identifier id = BuiltInRegistries.ITEM.getKey(this.result);

		this.save(recipeOutput, ElementalCraftApi.identifier(SawingRecipe.NAME + '/' + id.getPath()));
	}

	public void save(RecipeOutput recipeOutput, String save) {
		Identifier Identifier = BuiltInRegistries.ITEM.getKey(this.result);
		if (Identifier.parse(save).equals(Identifier)) {
			throw new IllegalStateException("Sawing Recipe " + save + " should remove its 'save' argument");
		} else {
			this.save(recipeOutput, ElementalCraftApi.identifier(SawingRecipe.NAME + '/' + save));
		}
	}

	public void save(RecipeOutput recipeOutput, Identifier id) {
		recipeOutput.accept(ResourceKey.create(Registries.RECIPE, id), new SawingRecipe(new Recipe.CommonInfo(false), elementAmount, luckRatio, this.ingredient, 1, new ItemStackTemplate(this.result, count)), null); // TODO input size
	}
}
