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
import sirttas.elementalcraft.recipe.instrument.io.sawing.SawingRecipe;

public class SawingRecipeBuilder {

	private final Item result;
	private Ingredient ingredient;
	private int elementAmount;
	private int luckRatio;
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

	public SawingRecipeBuilder withIngredient(TagKey<Item> tag) {
		return this.withIngredient(Ingredient.of(tag));
	}

	public SawingRecipeBuilder withIngredient(ItemLike item) {
		return this.withIngredient(Ingredient.of(item));
	}

	public SawingRecipeBuilder withIngredient(ItemStack stack) {
		return this.withIngredient(Ingredient.of(stack));
	}
	
	public SawingRecipeBuilder withIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
		return this;
	}

	public SawingRecipeBuilder withLuckRatio(int luckRatio) {
		this.luckRatio = luckRatio;
		return this;
	}

	public SawingRecipeBuilder withCount(int count) {
		this.count = count;
		return this;
	}

	public void save(RecipeOutput recipeOutput) {
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(this.result);

		this.save(recipeOutput, ElementalCraftApi.createRL(SawingRecipe.NAME + '/' + id.getPath()));
	}

	public void save(RecipeOutput recipeOutput, String save) {
		ResourceLocation resourcelocation = BuiltInRegistries.ITEM.getKey(this.result);
		if ((new ResourceLocation(save)).equals(resourcelocation)) {
			throw new IllegalStateException("Sawing Recipe " + save + " should remove its 'save' argument");
		} else {
			this.save(recipeOutput, ElementalCraftApi.createRL(SawingRecipe.NAME + '/' + save));
		}
	}

	public void save(RecipeOutput recipeOutput, ResourceLocation id) {
		recipeOutput.accept(id, new SawingRecipe(elementAmount, luckRatio, this.ingredient, new ItemStack(this.result, count)), null);
	}
}
