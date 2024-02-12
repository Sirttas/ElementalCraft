package sirttas.elementalcraft.datagen.recipe.builder.instrument;

import com.google.common.collect.Lists;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.instrument.InscriptionRecipe;

import java.util.ArrayList;
import java.util.List;

public class InscriptionRecipeBuilder {

	private final ResourceLocation output;
	private final List<Ingredient> ingredients = Lists.newArrayList();
	private final ElementType elementType;
	private int elementAmount;
	private Ingredient slate;

	public InscriptionRecipeBuilder(ResourceLocation output, ElementType elementType) {
		this.elementType = elementType;
		elementAmount = 5000;
		this.output = output;
	}

	public static InscriptionRecipeBuilder inscriptionRecipe(ResourceLocation output, ElementType elementType) {
		return new InscriptionRecipeBuilder(output, elementType);
	}

	public InscriptionRecipeBuilder withElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
		return this;
	}

	public InscriptionRecipeBuilder setSlate(TagKey<Item> tag) {
		return this.setSlate(Ingredient.of(tag));
	}

	public InscriptionRecipeBuilder setSlate(ItemLike item) {
		return this.setSlate(Ingredient.of(item));
	}

	public InscriptionRecipeBuilder setSlate(Ingredient ingredient) {
		slate = ingredient;
		return this;
	}

	public InscriptionRecipeBuilder addIngredient(TagKey<Item> tag) {
		return this.addIngredient(Ingredient.of(tag));
	}

	public InscriptionRecipeBuilder addIngredient(ItemLike item) {
		return this.addIngredient(Ingredient.of(item));
	}

	public InscriptionRecipeBuilder addIngredient(Ingredient ingredient) {
		this.ingredients.add(ingredient);
		return this;
	}

	public void save(RecipeOutput recipeOutput) {
		this.save(recipeOutput, output.getPath());
	}

	public void save(RecipeOutput recipeOutput, String save) {
		this.save(recipeOutput, ElementalCraftApi.createRL(InscriptionRecipe.NAME + '/' + save));
	}

	public void save(RecipeOutput recipeOutput, ResourceLocation id) {
		var i = new ArrayList<Ingredient>(ingredients.size() + 1);

		i.add(slate);
		i.addAll(ingredients);
		recipeOutput.accept(id, new InscriptionRecipe(elementType, elementAmount, i, ECItems.RUNE.get().getRuneStack(this.output)), null);
	}
}
