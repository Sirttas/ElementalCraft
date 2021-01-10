package sirttas.elementalcraft.datagen.recipe.builder.instrument;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.Collector;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe;

public class CrystallizationRecipeBuilder {

	private final Map<ItemStack, Integer> outputs;
	private final List<Ingredient> ingredients = Lists.newArrayList(Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY);
	private final ElementType elementType;
	private int elementAmount;
	private final IRecipeSerializer<?> serializer;

	public CrystallizationRecipeBuilder(IRecipeSerializer<?> serializerIn, ElementType elementType) {
		this.serializer = serializerIn;
		this.elementType = elementType;
		elementAmount = 5000;
		outputs = new HashMap<>();
	}

	public static CrystallizationRecipeBuilder crystallizationRecipe(ElementType elementType) {
		return new CrystallizationRecipeBuilder(CrystallizationRecipe.SERIALIZER, elementType);
	}

	public CrystallizationRecipeBuilder withElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
		return this;
	}

	public CrystallizationRecipeBuilder setGem(INamedTag<Item> tagIn) {
		return this.setIngredient(0, tagIn);
	}

	public CrystallizationRecipeBuilder setGem(IItemProvider itemIn) {
		return this.setIngredient(0, itemIn);
	}

	public CrystallizationRecipeBuilder setGem(Ingredient ingredientIn) {
		return this.setIngredient(0, ingredientIn);
	}

	public CrystallizationRecipeBuilder setCrystal(INamedTag<Item> tagIn) {
		return this.setIngredient(1, tagIn);
	}

	public CrystallizationRecipeBuilder setCrystal(IItemProvider itemIn) {
		return this.setIngredient(1, itemIn);
	}

	public CrystallizationRecipeBuilder setCrystal(Ingredient ingredientIn) {
		return this.setIngredient(1, ingredientIn);
	}

	public CrystallizationRecipeBuilder setShard(INamedTag<Item> tagIn) {
		return this.setIngredient(2, tagIn);
	}

	public CrystallizationRecipeBuilder setShard(IItemProvider itemIn) {
		return this.setIngredient(2, itemIn);
	}

	public CrystallizationRecipeBuilder setShard(Ingredient ingredientIn) {
		return this.setIngredient(2, ingredientIn);
	}

	private CrystallizationRecipeBuilder setIngredient(int index, INamedTag<Item> tagIn) {
		return this.setIngredient(index, Ingredient.fromTag(tagIn));
	}

	private CrystallizationRecipeBuilder setIngredient(int index, IItemProvider itemIn) {
		return this.setIngredient(index, Ingredient.fromItems(itemIn));
	}

	private CrystallizationRecipeBuilder setIngredient(int index, Ingredient ingredientIn) {
		this.ingredients.set(index, ingredientIn);
		return this;
	}

	public CrystallizationRecipeBuilder addOutput(IItemProvider item, int weight) {
		this.outputs.put(new ItemStack(item.asItem()), weight);
		return this;
	}

	public void build(Consumer<IFinishedRecipe> consumerIn, String save) {
		this.build(consumerIn, ElementalCraft.createRL(CrystallizationRecipe.NAME + '/' + save));
	}

	public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
		consumerIn.accept(new Result(id, this.serializer, this.ingredients, this.outputs, elementType, elementAmount));
	}

	public static class Result implements IFinishedRecipe {
		private final ResourceLocation id;
		private final List<Ingredient> ingredients;
		private final Map<ItemStack, Integer> outputs;
		private final ElementType elementType;
		private final int elementAmount;
		private final IRecipeSerializer<?> serializer;

		public Result(ResourceLocation idIn, IRecipeSerializer<?> serializerIn, List<Ingredient> ingredients, Map<ItemStack, Integer> outputs, ElementType elementType, int elementAmount) {
			this.id = idIn;
			this.serializer = serializerIn;
			this.ingredients = ingredients;
			this.outputs = outputs;
			this.elementType = elementType;
			this.elementAmount = elementAmount;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void serialize(JsonObject json) {
			json.addProperty(ECNames.ELEMENT_TYPE, this.elementType.getString());
			json.addProperty(ECNames.ELEMENT_AMOUNT, elementAmount);
			JsonObject ingredientsJson = new JsonObject();

			ingredientsJson.add(ECNames.GEM, this.ingredients.get(0).serialize());
			ingredientsJson.add(ECNames.CRYSTAL, this.ingredients.get(1).serialize());
			ingredientsJson.add(ECNames.SHARD, this.ingredients.get(2).serialize());

			json.add(ECNames.INGREDIENTS, ingredientsJson);
			
			json.add(ECNames.OUTPUTS, outputs.entrySet().stream().sorted(Comparator.comparingInt(e -> ((Entry<ItemStack, Integer>) e).getValue()).reversed())
					.map(e -> createOutput(e.getKey(), e.getValue())).collect(Collector.of(JsonArray::new, JsonArray::add, (array1, array2) -> {
						array1.addAll(array2);
						return array1;
					})));
		}

		private JsonObject createOutput(ItemStack stack, int weight) {
			JsonObject json = new JsonObject();

			json.addProperty(ECNames.ITEM, ForgeRegistries.ITEMS.getKey(stack.getItem()).toString());
			json.addProperty(ECNames.WEIGHT, weight);
			return json;
		}

		@Override
		public ResourceLocation getID() {
			return this.id;
		}

		@Override
		public IRecipeSerializer<?> getSerializer() {
			return this.serializer;
		}

		@Override
		@Nullable
		public JsonObject getAdvancementJson() {
			return null;
		}

		@Override
		@Nullable
		public ResourceLocation getAdvancementID() {
			return null;
		}
	}
}
