package sirttas.elementalcraft.datagen.recipe.builder;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.recipe.PureInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;

import java.util.EnumMap;
import java.util.Map;

public class PureInfusionRecipeBuilder {
	private final Item result;
	private final Map<ElementType, Ingredient> ingredients = new EnumMap<>(ElementType.class);
	private int elementAmount;

	public PureInfusionRecipeBuilder(ItemLike result) {
		this.result = result.asItem();
		elementAmount = 60000;
	}

	public static PureInfusionRecipeBuilder pureInfusionRecipe(ItemLike result) {
		return new PureInfusionRecipeBuilder(result);
	}

	public PureInfusionRecipeBuilder withElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
		return this;
	}

	public PureInfusionRecipeBuilder setIngredient(TagKey<Item> tag) {
		return this.setIngredient(ElementType.NONE, Ingredient.of(tag));
	}

	public PureInfusionRecipeBuilder setIngredient(ItemLike item) {
		return this.setIngredient(ElementType.NONE, Ingredient.of(item));
	}

	public PureInfusionRecipeBuilder setIngredient(Ingredient ingredient) {
		return this.setIngredient(ElementType.NONE, ingredient);
	}

	public PureInfusionRecipeBuilder setIngredient(ElementType type, TagKey<Item> tag) {
		return this.setIngredient(type, Ingredient.of(tag));
	}

	public PureInfusionRecipeBuilder setIngredient(ElementType type, ItemLike item) {
		return this.setIngredient(type, Ingredient.of(item));
	}

	
	public PureInfusionRecipeBuilder setIngredient(ElementType type, Ingredient ingredientIn) {
		this.ingredients.put(type, ingredientIn);
		return this;
	}

	
	public void save(RecipeOutput recipeOutput) {
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(this.result);

		this.save(recipeOutput, new ResourceLocation(id.getNamespace(), PureInfusionRecipe.NAME + '/' + id.getPath()));
	}

	public void save(RecipeOutput recipeOutput, String save) {
		ResourceLocation resourcelocation = BuiltInRegistries.ITEM.getKey(this.result);
		if ((new ResourceLocation(save)).equals(resourcelocation)) {
			throw new IllegalStateException("Binding Recipe " + save + " should remove its 'save' argument");
		} else {
			this.save(recipeOutput, ElementalCraftApi.createRL(AbstractBindingRecipe.NAME + '/' + save));
		}
	}

	public void save(RecipeOutput recipeOutput, ResourceLocation id) {
		recipeOutput.accept(id, new PureInfusionRecipe(elementAmount, this.ingredients, new ItemStack(this.result)), null);
	}
}
