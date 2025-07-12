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
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe;

import java.util.List;

public class CrystallizationRecipeBuilder {

	private final Item result;
	private final List<Ingredient> ingredients = Lists.newArrayList(Ingredient.EMPTY, Ingredient.EMPTY);
	private final ElementType elementType;
	private int elementAmount;

	public CrystallizationRecipeBuilder(ItemLike result, ElementType elementType) {
		this.result = result.asItem();
		this.elementType = elementType;
		elementAmount = 5000;
	}

	public static CrystallizationRecipeBuilder crystallizationRecipe(ItemLike result, ElementType elementType) {
		return new CrystallizationRecipeBuilder(result, elementType);
	}

	public CrystallizationRecipeBuilder withElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
		return this;
	}

	public CrystallizationRecipeBuilder setGem(TagKey<Item> tag) {
		return this.setIngredient(0, tag);
	}

	public CrystallizationRecipeBuilder setGem(ItemLike item) {
		return this.setIngredient(0, item);
	}

	public CrystallizationRecipeBuilder setGem(Ingredient ingredient) {
		return this.setIngredient(0, ingredient);
	}

	public CrystallizationRecipeBuilder setCrystal(TagKey<Item> tag) {
		return this.setIngredient(1, tag);
	}

	public CrystallizationRecipeBuilder setCrystal(ItemLike item) {
		return this.setIngredient(1, item);
	}

	public CrystallizationRecipeBuilder setCrystal(Ingredient ingredient) {
		return this.setIngredient(1, ingredient);
	}

	private CrystallizationRecipeBuilder setIngredient(int index, TagKey<Item> tag) {
		return this.setIngredient(index, Ingredient.of(tag));
	}

	private CrystallizationRecipeBuilder setIngredient(int index, ItemLike item) {
		return this.setIngredient(index, Ingredient.of(item));
	}

	private CrystallizationRecipeBuilder setIngredient(int index, Ingredient ingredient) {
		this.ingredients.set(index, ingredient);
		return this;
	}

	public void save(RecipeOutput recipeOutput) {
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(this.result);

		this.save(recipeOutput, ResourceLocation.fromNamespaceAndPath(id.getNamespace(), CrystallizationRecipe.NAME + '/' + id.getPath()));
	}

	public void save(RecipeOutput recipeOutput, String save) {
		ResourceLocation resourcelocation = BuiltInRegistries.ITEM.getKey(this.result);
		if (ResourceLocation.parse(save).equals(resourcelocation)) {
			throw new IllegalStateException("Crystalization Recipe " + save + " should remove its 'save' argument");
		} else {
			this.save(recipeOutput, ElementalCraftApi.createRL(CrystallizationRecipe.NAME + '/' + save));
		}
	}

	public void save(RecipeOutput recipeOutput, ResourceLocation id) {
		recipeOutput.accept(id, new CrystallizationRecipe(elementType, elementAmount, this.ingredients, new ItemStack(this.result)), null);
	}
}
