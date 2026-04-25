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
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.recipe.instrument.crystallization.CrystallizationRecipe;

public class CrystallizationRecipeBuilder {

	private final Item result;
	private Ingredient gem;
	private Ingredient crystal;
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

	public CrystallizationRecipeBuilder setGem(ItemLike item) {
		return this.setGem(Ingredient.of(item));
	}

	public CrystallizationRecipeBuilder setGem(Ingredient ingredient) {
		this.gem = ingredient;
		return this;
	}

	public CrystallizationRecipeBuilder setCrystal(ItemLike item) {
		return this.setCrystal(Ingredient.of(item));
	}

	public CrystallizationRecipeBuilder setCrystal(Ingredient ingredient) {
		this.crystal = ingredient;
		return this;
	}

	public void save(RecipeOutput recipeOutput) {
		Identifier id = BuiltInRegistries.ITEM.getKey(this.result);

		this.save(recipeOutput, Identifier.fromNamespaceAndPath(id.getNamespace(), CrystallizationRecipe.NAME + '/' + id.getPath()));
	}

	public void save(RecipeOutput recipeOutput, String save) {
		Identifier Identifier = BuiltInRegistries.ITEM.getKey(this.result);
		if (Identifier.parse(save).equals(Identifier)) {
			throw new IllegalStateException("Crystalization Recipe " + save + " should remove its 'save' argument");
		} else {
			this.save(recipeOutput, ElementalCraftApi.createRL(CrystallizationRecipe.NAME + '/' + save));
		}
	}

	public void save(RecipeOutput recipeOutput, Identifier id) {
		recipeOutput.accept(ResourceKey.create(Registries.RECIPE, id), new CrystallizationRecipe(new Recipe.CommonInfo(false), elementType, elementAmount, this.gem, this.crystal, new ItemStackTemplate(this.result)), null);
	}
}
