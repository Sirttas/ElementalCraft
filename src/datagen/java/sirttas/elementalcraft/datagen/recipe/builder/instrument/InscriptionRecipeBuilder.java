package sirttas.elementalcraft.datagen.recipe.builder.instrument;

import com.google.common.collect.Lists;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.instrument.inscription.InscriptionRecipe;

import java.util.ArrayList;
import java.util.List;

public class InscriptionRecipeBuilder {

	private final Identifier output;
	private final List<Ingredient> ingredients = Lists.newArrayList();
	private final ElementType elementType;
	private int elementAmount;
	private Ingredient slate;

	public InscriptionRecipeBuilder(Identifier output, ElementType elementType) {
		this.elementType = elementType;
		elementAmount = 5000;
		this.output = output;
	}

	public static InscriptionRecipeBuilder inscriptionRecipe(ResourceKey<Rune> output, ElementType elementType) {
		return inscriptionRecipe(output.identifier(), elementType);
	}

	public static InscriptionRecipeBuilder inscriptionRecipe(Identifier output, ElementType elementType) {
		return new InscriptionRecipeBuilder(output, elementType);
	}

	public InscriptionRecipeBuilder withElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
		return this;
	}

	public InscriptionRecipeBuilder setSlate(ItemLike item) {
		return this.setSlate(Ingredient.of(item));
	}

	public InscriptionRecipeBuilder setSlate(Ingredient ingredient) {
		slate = ingredient;
		return this;
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
		this.save(recipeOutput, ElementalCraftApi.identifier(InscriptionRecipe.NAME + '/' + save));
	}

	public void save(RecipeOutput recipeOutput, Identifier id) {
		var i = new ArrayList<Ingredient>(ingredients.size() + 1);

		i.add(slate);
		i.addAll(ingredients);
		recipeOutput.accept(ResourceKey.create(Registries.RECIPE, id), new InscriptionRecipe(new Recipe.CommonInfo(false), elementType, elementAmount, i, ECItems.RUNE.get().getRuneStackTemplate(this.output)), null);
	}
}
