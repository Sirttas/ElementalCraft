package sirttas.elementalcraft.datagen.recipe.builder.instrument.infusion;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.recipe.instrument.infusion.InfusionRecipe;

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
	protected ResourceLocation getId() {
		return BuiltInRegistries.ITEM.getKey(this.result);
	}
	
	@Override
	public void save(RecipeOutput recipeOutput, ResourceLocation id) {
		recipeOutput.accept(id, new InfusionRecipe(elementType, elementAmount, this.ingredient, new ItemStack(this.result)), null);
	}
}
