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
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
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

	public CrystallizationRecipeBuilder(RecipeSerializer<?> serializer, ElementType elementType) {
		this.serializer = serializer;
		this.elementType = elementType;
		elementAmount = 5000;
		outputs = Lists.newArrayList();
	}

	public static CrystallizationRecipeBuilder crystallizationRecipe(ElementType elementType) {
		return new CrystallizationRecipeBuilder(ECRecipeSerializers.CRYSTALLIZATION.get(), elementType);
	}

	public CrystallizationRecipeBuilder withElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
		return this;
	}

	public CrystallizationRecipeBuilder setGem(TagKey<Item> tag) {
		return this.setIngredient(0, tag);
	}

	public CrystallizationRecipeBuilder setGem(ItemLike item) {
		return this.setIngredient(0, item);
	}

	public CrystallizationRecipeBuilder setGem(Ingredient ingredient) {
		return this.setIngredient(0, ingredient);
	}

	public CrystallizationRecipeBuilder setCrystal(TagKey<Item> tag) {
		return this.setIngredient(1, tag);
	}

	public CrystallizationRecipeBuilder setCrystal(ItemLike item) {
		return this.setIngredient(1, item);
	}

	public CrystallizationRecipeBuilder setCrystal(Ingredient ingredient) {
		return this.setIngredient(1, ingredient);
	}

	public CrystallizationRecipeBuilder setShard(TagKey<Item> tag) {
		return this.setIngredient(2, tag);
	}

	public CrystallizationRecipeBuilder setShard(ItemLike item) {
		return this.setIngredient(2, item);
	}

	public CrystallizationRecipeBuilder setShard(Ingredient ingredient) {
		return this.setIngredient(2, ingredient);
	}

	private CrystallizationRecipeBuilder setIngredient(int index, TagKey<Item> tag) {
		return this.setIngredient(index, Ingredient.of(tag));
	}

	private CrystallizationRecipeBuilder setIngredient(int index, ItemLike item) {
		return this.setIngredient(index, Ingredient.of(item));
	}

	private CrystallizationRecipeBuilder setIngredient(int index, Ingredient ingredient) {
		this.ingredients.set(index, ingredient);
		return this;
	}

	public CrystallizationRecipeBuilder addOutput(ItemLike item, float weight) {
		return addOutput(item, weight, 1);
	}

	public CrystallizationRecipeBuilder addOutput(ItemLike item, float weight, float quality) {
		this.outputs.add(CrystallizationRecipe.createResult(new ItemStack(item), weight, quality));
		return this;
	}

	public void save(Consumer<FinishedRecipe> consumer, String save) {
		this.save(consumer, ElementalCraft.createRL(CrystallizationRecipe.NAME + '/' + save));
	}

	public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
		consumer.accept(new Result(id, this.serializer, this.ingredients, this.outputs, elementType, elementAmount));
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
