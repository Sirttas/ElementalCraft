package sirttas.elementalcraft.datagen.recipe.builder;

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
import sirttas.elementalcraft.recipe.pure.infusion.PureInfusionRecipe;

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

	public PureInfusionRecipeBuilder setIngredient(ItemLike item) {
		return this.setIngredient(ElementType.NONE, Ingredient.of(item));
	}

	public PureInfusionRecipeBuilder setIngredient(Ingredient ingredient) {
		return this.setIngredient(ElementType.NONE, ingredient);
	}

	public PureInfusionRecipeBuilder setIngredient(ElementType type, ItemLike item) {
		return this.setIngredient(type, Ingredient.of(item));
	}

	
	public PureInfusionRecipeBuilder setIngredient(ElementType type, Ingredient ingredientIn) {
		this.ingredients.put(type, ingredientIn);
		return this;
	}
	
	public void save(RecipeOutput recipeOutput) {
		Identifier id = BuiltInRegistries.ITEM.getKey(this.result);

		this.save(recipeOutput, Identifier.fromNamespaceAndPath(id.getNamespace(), PureInfusionRecipe.NAME + '/' + id.getPath()));
	}

	public void save(RecipeOutput recipeOutput, String save) {
		Identifier Identifier = BuiltInRegistries.ITEM.getKey(this.result);
		if (Identifier.parse(save).equals(Identifier)) {
			throw new IllegalStateException("Pure Infusion Recipe " + save + " should remove its 'save' argument");
		} else {
			this.save(recipeOutput, ElementalCraftApi.identifier(PureInfusionRecipe.NAME + '/' + save));
		}
	}

	public void save(RecipeOutput recipeOutput, Identifier id) {
		recipeOutput.accept(ResourceKey.create(Registries.RECIPE, id), new PureInfusionRecipe(new Recipe.CommonInfo(false), elementAmount, this.ingredients, new ItemStackTemplate(this.result)), null);
	}
}
