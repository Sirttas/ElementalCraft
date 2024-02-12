package sirttas.elementalcraft.datagen.recipe.builder.instrument;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.recipe.instrument.io.grinding.GrindingRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

public class GrindingRecipeBuilder {
	
	private final Item result;
	private Ingredient ingredient;
	private int elementAmount;
	private int luckRatio;
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

	public GrindingRecipeBuilder withIngredient(TagKey<Item> tag) {
		return this.withIngredient(Ingredient.of(tag));
	}

	public GrindingRecipeBuilder withIngredient(ItemLike item) {
		return this.withIngredient(Ingredient.of(item));
	}

	public GrindingRecipeBuilder withIngredient(ItemStack stack) {
		return this.withIngredient(Ingredient.of(stack));
	}
	
	public GrindingRecipeBuilder withIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
		return this;
	}

	public GrindingRecipeBuilder withLuckRatio(int luckRatio) {
		this.luckRatio = luckRatio;
		return this;
	}

	public GrindingRecipeBuilder withCount(int count) {
		this.count = count;
		return this;
	}

	public void save(RecipeOutput recipeOutput) {
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(this.result);

		this.save(recipeOutput, ElementalCraftApi.createRL(IGrindingRecipe.NAME + '/' + id.getPath()));
	}

	public void save(RecipeOutput recipeOutput, String save) {
		ResourceLocation resourcelocation = BuiltInRegistries.ITEM.getKey(this.result);
		if ((new ResourceLocation(save)).equals(resourcelocation)) {
			throw new IllegalStateException("Grinding Recipe " + save + " should remove its 'save' argument");
		} else {
			this.save(recipeOutput, ElementalCraftApi.createRL(IGrindingRecipe.NAME + '/' + save));
		}
	}

	public void save(RecipeOutput recipeOutput, ResourceLocation id) {
		recipeOutput.accept(id, new GrindingRecipe(elementAmount, luckRatio, this.ingredient, new ItemStack(this.result, count)), null);
	}
}
