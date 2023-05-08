package sirttas.elementalcraft.datagen.recipe.builder.instrument.infusion;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class ToolInfusionRecipeBuilder extends AbstractInfusionRecipeBuilder {
	
	private final ResourceLocation infusion;
	private final String prefix;
	
	
	public ToolInfusionRecipeBuilder(TagKey<Item> ingredient, ResourceLocation infusion) {
		super(ECRecipeSerializers.TOOL_INFUSION.get(), Ingredient.of(ingredient));
		String[] split = ingredient.location().getPath().split("/");
		
		this.prefix = "tool/" + split[split.length - 1] + "_";
		this.infusion = infusion;
		elementAmount = 2000;
	}

	public ToolInfusionRecipeBuilder(ItemLike ingredient, ResourceLocation infusion) {
		super(ECRecipeSerializers.TOOL_INFUSION.get(), Ingredient.of(ingredient));
		String[] split = ForgeRegistries.ITEMS.getKey(ingredient.asItem()).getPath().split("/");

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
		return ElementalCraft.createRL(ForgeRegistries.ENCHANTMENTS.getKey(enchantment).getPath());
	}

	@Override
	protected ResourceLocation getId() {
		String namespace = infusion.getNamespace();
		
		return new ResourceLocation(namespace.equals("minecraft") ? ElementalCraftApi.MODID : namespace, prefix + infusion.getPath());
	}
	
	@Override
	public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
		consumer.accept(new Result(id, this.serializer, this.ingredient, this.infusion, elementAmount));
	}

	public static class Result extends AbstractResult {
		
		private final ResourceLocation infusion;

		public Result(ResourceLocation id, RecipeSerializer<?> serializer, Ingredient ingredient, ResourceLocation infusion, int elementAmount) {
			super(id, serializer, ingredient, elementAmount);
			this.infusion = infusion;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.addProperty(ECNames.TOOL_INFUSION, infusion.toString());
			super.serializeRecipeData(json);
		}
	}
}
