package sirttas.elementalcraft.datagen.recipe.builder.instrument.infusion;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.recipe.instrument.infusion.ToolInfusionRecipe;

import javax.annotation.Nonnull;

public class ToolInfusionRecipeBuilder extends AbstractInfusionRecipeBuilder {
	
	private final Identifier infusion;
	private final String prefix;
	
	
	public ToolInfusionRecipeBuilder(HolderGetter<Item> items, TagKey<Item> ingredient, Identifier infusion) {
		super(Ingredient.of(items.getOrThrow(ingredient)));
		String[] split = ingredient.location().getPath().split("/");
		
		this.prefix = "tool/" + split[split.length - 1] + "_";
		this.infusion = infusion;
		elementAmount = 2000;
	}

	public ToolInfusionRecipeBuilder(ItemLike ingredient, Identifier infusion) {
		super(Ingredient.of(ingredient));
		String[] split = BuiltInRegistries.ITEM.getKey(ingredient.asItem()).getPath().split("/");

		this.prefix = "tool/" + split[split.length - 1] + "_";
		this.infusion = infusion;
		elementAmount = 2000;
	}


	public static ToolInfusionRecipeBuilder toolInfusionRecipe(ItemLike ingredient, Identifier infusion) {
		return new ToolInfusionRecipeBuilder(ingredient, infusion);
	}

	public static ToolInfusionRecipeBuilder toolInfusionRecipe(HolderGetter<Item> items, TagKey<Item> ingredient, Identifier infusion) {
		return new ToolInfusionRecipeBuilder(items, ingredient, infusion);
	}

	public static ToolInfusionRecipeBuilder toolInfusionRecipe(ItemLike ingredient, ResourceKey<Enchantment> enchantment) {
		return toolInfusionRecipe(ingredient, getEnchantmentName(enchantment));
	}

	public static ToolInfusionRecipeBuilder toolInfusionRecipe(HolderGetter<Item> items, TagKey<Item> ingredient, ResourceKey<Enchantment> enchantment) {
		return toolInfusionRecipe(items, ingredient, getEnchantmentName(enchantment));
	}

	@Nonnull
	private static Identifier getEnchantmentName(ResourceKey<Enchantment> enchantment) {
		return ElementalCraftApi.identifier(enchantment.identifier().getPath());
	}

	@Override
	protected Identifier getId() {
		String namespace = infusion.getNamespace();
		
		return Identifier.fromNamespaceAndPath(namespace.equals("minecraft") ? ElementalCraftApi.MODID : namespace, prefix + infusion.getPath());
	}
	
	@Override
	public void save(RecipeOutput recipeOutput, Identifier id) {
		recipeOutput.accept(ResourceKey.create(Registries.RECIPE, id), new ToolInfusionRecipe(new Recipe.CommonInfo(false), elementAmount, ElementalCraftApi.TOOL_INFUSION_MANAGER.getOrCreateHolder(infusion), this.ingredient), null);
	}
}
