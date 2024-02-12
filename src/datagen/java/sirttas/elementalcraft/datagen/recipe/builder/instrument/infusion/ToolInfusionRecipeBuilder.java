package sirttas.elementalcraft.datagen.recipe.builder.instrument.infusion;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.recipe.instrument.infusion.ToolInfusionRecipe;

import javax.annotation.Nonnull;

public class ToolInfusionRecipeBuilder extends AbstractInfusionRecipeBuilder {
	
	private final ResourceLocation infusion;
	private final String prefix;
	
	
	public ToolInfusionRecipeBuilder(TagKey<Item> ingredient, ResourceLocation infusion) {
		super(Ingredient.of(ingredient));
		String[] split = ingredient.location().getPath().split("/");
		
		this.prefix = "tool/" + split[split.length - 1] + "_";
		this.infusion = infusion;
		elementAmount = 2000;
	}

	public ToolInfusionRecipeBuilder(ItemLike ingredient, ResourceLocation infusion) {
		super(Ingredient.of(ingredient));
		String[] split = BuiltInRegistries.ITEM.getKey(ingredient.asItem()).getPath().split("/");

		this.prefix = "tool/" + split[split.length - 1] + "_";
		this.infusion = infusion;
		elementAmount = 2000;
	}


	public static ToolInfusionRecipeBuilder toolInfusionRecipe(ItemLike ingredient, ResourceLocation infusion) {
		return new ToolInfusionRecipeBuilder(ingredient, infusion);
	}

	public static ToolInfusionRecipeBuilder toolInfusionRecipe(TagKey<Item> ingredient, ResourceLocation infusion) {
		return new ToolInfusionRecipeBuilder(ingredient, infusion);
	}
	
	public static ToolInfusionRecipeBuilder toolInfusionRecipe(ItemLike ingredient, Enchantment enchantment) {
		return toolInfusionRecipe(ingredient, getEnchantmentName(enchantment));
	}

	public static ToolInfusionRecipeBuilder toolInfusionRecipe(TagKey<Item> ingredient, Enchantment enchantment) {
		return toolInfusionRecipe(ingredient, getEnchantmentName(enchantment));
	}

	@Nonnull
	private static ResourceLocation getEnchantmentName(Enchantment enchantment) {
		return ElementalCraftApi.createRL(BuiltInRegistries.ENCHANTMENT.getKey(enchantment).getPath());
	}

	@Override
	protected ResourceLocation getId() {
		String namespace = infusion.getNamespace();
		
		return new ResourceLocation(namespace.equals("minecraft") ? ElementalCraftApi.MODID : namespace, prefix + infusion.getPath());
	}
	
	@Override
	public void save(RecipeOutput recipeOutput, ResourceLocation id) {
		recipeOutput.accept(id, new ToolInfusionRecipe(ElementalCraftApi.TOOL_INFUSION_MANAGER.getOrCreateHolder(infusion), this.ingredient, elementAmount), null);
	}
}
