package sirttas.elementalcraft.datagen.recipe.builder.instrument;

import com.google.common.collect.Lists;
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

	public BindingRecipeBuilder addIngredient(ItemLike item) {
		return this.addIngredient(Ingredient.of(item));
	}

	public BindingRecipeBuilder addIngredient(Ingredient ingredient) {
		this.ingredients.add(ingredient);
		return this;
	}


	public void save(RecipeOutput recipeOutput) {
		Identifier id = BuiltInRegistries.ITEM.getKey(this.result);

		this.save(recipeOutput, Identifier.fromNamespaceAndPath(id.getNamespace(), AbstractBindingRecipe.NAME + '/' + id.getPath()));
	}

	public void save(RecipeOutput recipeOutput, String save) {
		Identifier Identifier = BuiltInRegistries.ITEM.getKey(this.result);
		if (Identifier.parse(save).equals(Identifier)) {
			throw new IllegalStateException("Binding Recipe " + save + " should remove its 'save' argument");
		} else {
			this.save(recipeOutput, ElementalCraftApi.identifier(AbstractBindingRecipe.NAME + '/' + save));
		}
	}

	public void save(RecipeOutput recipeOutput, Identifier id) {
		recipeOutput.accept(ResourceKey.create(Registries.RECIPE, id), new BindingRecipe(new Recipe.CommonInfo(false), elementType, elementAmount, this.ingredients, new ItemStackTemplate(this.result)), null);
	}
}
