package sirttas.elementalcraft.datagen.recipe.builder.instrument;

import com.google.common.collect.Lists;
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
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.BindingRecipe;

import java.util.List;

public class BindingRecipeBuilder {
	private final Item result;
	private final List<Ingredient> ingredients = Lists.newArrayList();
	private final ElementType elementType;
	private int elementAmount;

	public BindingRecipeBuilder(ItemLike result, ElementType elementType) {
		this.result = result.asItem();
		this.elementType = elementType;
		elementAmount = 2500;
	}

	public static BindingRecipeBuilder bindingRecipe(ItemLike result, ElementType elementType) {
		return new BindingRecipeBuilder(result, elementType);
	}

	public BindingRecipeBuilder withElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
		return this;
	}

	public BindingRecipeBuilder addIngredient(TagKey<Item> tag) {
		return this.addIngredient(Ingredient.of(tag));
	}

	public BindingRecipeBuilder addIngredient(ItemLike item) {
		return this.addIngredient(Ingredient.of(item));
	}

	public BindingRecipeBuilder addIngredient(Ingredient ingredient) {
		this.ingredients.add(ingredient);
		return this;
	}


	public void save(RecipeOutput recipeOutput) {
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(this.result);

		this.save(recipeOutput, new ResourceLocation(id.getNamespace(), AbstractBindingRecipe.NAME + '/' + id.getPath()));
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
		recipeOutput.accept(id, new BindingRecipe(elementType, elementAmount, this.ingredients, new ItemStack(this.result)), null);
	}
}
