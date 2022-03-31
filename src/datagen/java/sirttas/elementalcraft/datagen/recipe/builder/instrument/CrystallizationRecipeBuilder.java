package sirttas.elementalcraft.datagen.recipe.builder.instrument;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import sirttas.dpanvil.api.codec.CodecHelper;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.datagen.recipe.builder.AbstractFinishedRecipe;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe.ResultEntry;

import java.util.List;
import java.util.function.Consumer;

public class CrystallizationRecipeBuilder {

	private final List<ResultEntry> outputs;
	private final List<Ingredient> ingredients = Lists.newArrayList(Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY);
	private final ElementType elementType;
	private int elementAmount;
	private final RecipeSerializer<?> serializer;

	public CrystallizationRecipeBuilder(RecipeSerializer<?> serializerIn, ElementType elementType) {
		this.serializer = serializerIn;
		this.elementType = elementType;
		elementAmount = 5000;
		outputs = Lists.newArrayList();
	}

	public static CrystallizationRecipeBuilder crystallizationRecipe(ElementType elementType) {
		return new CrystallizationRecipeBuilder(CrystallizationRecipe.SERIALIZER, elementType);
	}

	public CrystallizationRecipeBuilder withElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
		return this;
	}

	public CrystallizationRecipeBuilder setGem(TagKey<Item> tag) {
		return this.setIngredient(0, tag);
	}

	public CrystallizationRecipeBuilder setGem(ItemLike itemIn) {
		return this.setIngredient(0, itemIn);
	}

	public CrystallizationRecipeBuilder setGem(Ingredient ingredientIn) {
		return this.setIngredient(0, ingredientIn);
	}

	public CrystallizationRecipeBuilder setCrystal(TagKey<Item> tagIn) {
		return this.setIngredient(1, tagIn);
	}

	public CrystallizationRecipeBuilder setCrystal(ItemLike itemIn) {
		return this.setIngredient(1, itemIn);
	}

	public CrystallizationRecipeBuilder setCrystal(Ingredient ingredientIn) {
		return this.setIngredient(1, ingredientIn);
	}

	public CrystallizationRecipeBuilder setShard(TagKey<Item> tag) {
		return this.setIngredient(2, tag);
	}

	public CrystallizationRecipeBuilder setShard(ItemLike itemIn) {
		return this.setIngredient(2, itemIn);
	}

	public CrystallizationRecipeBuilder setShard(Ingredient ingredientIn) {
		return this.setIngredient(2, ingredientIn);
	}

	private CrystallizationRecipeBuilder setIngredient(int index, TagKey<Item> tag) {
		return this.setIngredient(index, Ingredient.of(tag));
	}

	private CrystallizationRecipeBuilder setIngredient(int index, ItemLike itemIn) {
		return this.setIngredient(index, Ingredient.of(itemIn));
	}

	private CrystallizationRecipeBuilder setIngredient(int index, Ingredient ingredientIn) {
		this.ingredients.set(index, ingredientIn);
		return this;
	}

	public CrystallizationRecipeBuilder addOutput(ItemLike item, float weight) {
		return addOutput(item, weight, 1);
	}

	public CrystallizationRecipeBuilder addOutput(ItemLike item, float weight, float quality) {
		this.outputs.add(CrystallizationRecipe.createResult(new ItemStack(item), weight, quality));
		return this;
	}

	public void build(Consumer<FinishedRecipe> consumerIn, String save) {
		this.build(consumerIn, ElementalCraft.createRL(CrystallizationRecipe.NAME + '/' + save));
	}

	public void build(Consumer<FinishedRecipe> consumerIn, ResourceLocation id) {
		consumerIn.accept(new Result(id, this.serializer, this.ingredients, this.outputs, elementType, elementAmount));
	}

	public static class Result extends AbstractFinishedRecipe {
		private final List<Ingredient> ingredients;
		private final List<ResultEntry> outputs;
		private final ElementType elementType;
		private final int elementAmount;

		public Result(ResourceLocation id, RecipeSerializer<?> serializer, List<Ingredient> ingredients, List<ResultEntry> outputs, ElementType elementType, int elementAmount) {
			super(id, serializer);
			this.ingredients = ingredients;
			this.outputs = outputs;
			this.elementType = elementType;
			this.elementAmount = elementAmount;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.addProperty(ECNames.ELEMENT_TYPE, this.elementType.getSerializedName());
			json.addProperty(ECNames.ELEMENT_AMOUNT, elementAmount);
			JsonObject ingredientsJson = new JsonObject();

			ingredientsJson.add(ECNames.GEM, this.ingredients.get(0).toJson());
			ingredientsJson.add(ECNames.CRYSTAL, this.ingredients.get(1).toJson());
			ingredientsJson.add(ECNames.SHARD, this.ingredients.get(2).toJson());

			json.add(ECNames.INGREDIENTS, ingredientsJson);
			json.add(ECNames.OUTPUTS, CodecHelper.encode(ResultEntry.LIST_CODEC, outputs));
		}
	}
}
