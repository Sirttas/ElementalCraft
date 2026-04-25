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
import sirttas.elementalcraft.recipe.instrument.io.grinding.GrindingRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.SimpleGrindingRecipe;

public class GrindingRecipeBuilder {
	
	private final Item result;
	private Ingredient ingredient;
	private int elementAmount;
	private double luckRatio;
	private int count;

	public GrindingRecipeBuilder(ItemLike result) {
		this.result = result.asItem();
		elementAmount = 1000;
		luckRatio = 0;
		count = 1;
	}

	public static GrindingRecipeBuilder grindingRecipe(ItemLike result) {
		return new GrindingRecipeBuilder(result);
	}
	
	public GrindingRecipeBuilder withElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
		return this;
	}

	public GrindingRecipeBuilder withIngredient(ItemLike item) {
		return this.withIngredient(Ingredient.of(item));
	}
	
	public GrindingRecipeBuilder withIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
		return this;
	}

	public GrindingRecipeBuilder withLuckRatio(double luckRatio) {
		this.luckRatio = luckRatio;
		return this;
	}

	public GrindingRecipeBuilder withCount(int count) {
		this.count = count;
		return this;
	}

	public void save(RecipeOutput recipeOutput) {
		Identifier id = BuiltInRegistries.ITEM.getKey(this.result);

		this.save(recipeOutput, ElementalCraftApi.createRL(GrindingRecipe.NAME + '/' + id.getPath()));
	}

	public void save(RecipeOutput recipeOutput, String save) {
		Identifier Identifier = BuiltInRegistries.ITEM.getKey(this.result);
		if (Identifier.parse(save).equals(Identifier)) {
			throw new IllegalStateException("Grinding Recipe " + save + " should remove its 'save' argument");
		} else {
			this.save(recipeOutput, ElementalCraftApi.createRL(GrindingRecipe.NAME + '/' + save));
		}
	}

	public void save(RecipeOutput recipeOutput, Identifier id) {
		recipeOutput.accept(ResourceKey.create(Registries.RECIPE, id), new SimpleGrindingRecipe(new Recipe.CommonInfo(false), elementAmount, luckRatio, this.ingredient, 1, new ItemStackTemplate(this.result, count)), null); // TODO input size
	}
}
