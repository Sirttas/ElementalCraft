package sirttas.elementalcraft.datagen.recipe.builder.instrument.infusion;

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
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.recipe.instrument.infusion.SimpleInfusionRecipe;

public class InfusionRecipeBuilder extends AbstractInfusionRecipeBuilder {
	
	private final Item result;
	protected final ElementType elementType;
	
	public InfusionRecipeBuilder(Ingredient ingredient, ItemLike result, ElementType elementType) {
		super(ingredient);
		this.result = result.asItem();
		this.elementType = elementType;
	}

	public static InfusionRecipeBuilder infusionRecipe(Ingredient ingredient, ItemLike result, ElementType elementType) {
		return new InfusionRecipeBuilder(ingredient, result, elementType);
	}

	@Override
	protected Identifier getId() {
		return BuiltInRegistries.ITEM.getKey(this.result);
	}
	
	@Override
	public void save(RecipeOutput recipeOutput, Identifier id) {
		recipeOutput.accept(ResourceKey.create(Registries.RECIPE, id), new SimpleInfusionRecipe(new Recipe.CommonInfo(false), elementType, elementAmount, this.ingredient, new ItemStackTemplate(this.result)), null);
	}
}
