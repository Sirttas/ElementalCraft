package sirttas.elementalcraft.datagen.recipe.builder.instrument.infusion;

import java.util.function.Consumer;

import com.google.gson.JsonObject;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.tags.Tag.Named;
import net.minecraft.resources.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.recipe.instrument.infusion.ToolInfusionRecipe;

public class ToolInfusionRecipeBuilder extends AbstractInfusionRecipeBuilder {
	
	private final ResourceLocation infusion;
	private final String prefix;
	
	
	public ToolInfusionRecipeBuilder(RecipeSerializer<?> serializerIn, Named<Item> ingredientIn, ResourceLocation infusion) {
		super(serializerIn, Ingredient.of(ingredientIn));
		String[] split = ingredientIn.getName().getPath().split("/");
		
		this.prefix = "tool/" + split[split.length - 1] + "_";
		this.infusion = infusion;
		elementAmount = 2000;
	}

	public static ToolInfusionRecipeBuilder toolInfusionRecipe(Named<Item> ingredientIn, ResourceLocation infusion) {
		return new ToolInfusionRecipeBuilder(ToolInfusionRecipe.SERIALIZER, ingredientIn, infusion);
	}
	
	public static ToolInfusionRecipeBuilder toolInfusionRecipe(Named<Item> ingredientIn, Enchantment enchantment) {
		return new ToolInfusionRecipeBuilder(ToolInfusionRecipe.SERIALIZER, ingredientIn, ElementalCraft.createRL(enchantment.getRegistryName().getPath()));
	}
	
	@Override
	protected ResourceLocation getId() {
		String namespace = infusion.getNamespace();
		
		return new ResourceLocation(namespace.equals("minecraft") ? ElementalCraftApi.MODID : namespace, prefix + infusion.getPath());
	}
	
	@Override
	public void build(Consumer<FinishedRecipe> consumerIn, ResourceLocation id) {
		consumerIn.accept(new Result(id, this.serializer, this.ingredient, this.infusion, elementAmount));
	}

	public static class Result extends AbstractResult {
		
		private final ResourceLocation infusion;

		public Result(ResourceLocation idIn, RecipeSerializer<?> serializerIn, Ingredient ingredientIn, ResourceLocation infusion, int elementAmount) {
			super(idIn, serializerIn, ingredientIn, elementAmount);
			this.infusion = infusion;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.addProperty(ECNames.TOOL_INFUSION, infusion.toString());
			super.serializeRecipeData(json);
		}
	}
}
