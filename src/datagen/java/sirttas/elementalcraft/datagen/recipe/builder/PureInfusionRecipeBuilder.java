package sirttas.elementalcraft.datagen.recipe.builder;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.PureInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;

import java.util.List;
import java.util.function.Consumer;

public class PureInfusionRecipeBuilder {
	private final Item result;
	private final List<Ingredient> ingredients = Lists.newArrayList(Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY);
	private int elementAmount;
	private final RecipeSerializer<?> serializer;

	public PureInfusionRecipeBuilder(RecipeSerializer<?> serializer, ItemLike result) {
		this.serializer = serializer;
		this.result = result.asItem();
		elementAmount = 60000;
	}

	public static PureInfusionRecipeBuilder pureInfusionRecipe(ItemLike result) {
		return new PureInfusionRecipeBuilder(ECRecipeSerializers.PURE_INFUSION.get(), result);
	}

	public PureInfusionRecipeBuilder withElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
		return this;
	}

	public PureInfusionRecipeBuilder setIngredient(TagKey<Item> tag) {
		return this.setIngredient(ElementType.NONE, Ingredient.of(tag));
	}

	public PureInfusionRecipeBuilder setIngredient(ItemLike item) {
		return this.setIngredient(ElementType.NONE, Ingredient.of(item));
	}

	public PureInfusionRecipeBuilder setIngredient(Ingredient ingredient) {
		return this.setIngredient(ElementType.NONE, ingredient);
	}

	public PureInfusionRecipeBuilder setIngredient(ElementType type, TagKey<Item> tag) {
		return this.setIngredient(type, Ingredient.of(tag));
	}

	public PureInfusionRecipeBuilder setIngredient(ElementType type, ItemLike item) {
		return this.setIngredient(type, Ingredient.of(item));
	}

	
	public PureInfusionRecipeBuilder setIngredient(ElementType type, Ingredient ingredientIn) {
		this.ingredients.set(getIndex(type), ingredientIn);
		return this;
	}

	private int getIndex(ElementType type) {
		return switch (type) {
			case NONE -> 0;
			case WATER -> 1;
			case FIRE -> 2;
			case EARTH -> 3;
			case AIR -> 4;
			default -> -1;
		};
	}
	
	public void save(Consumer<FinishedRecipe> consumer) {
		ResourceLocation id = ForgeRegistries.ITEMS.getKey(this.result);

		this.save(consumer, new ResourceLocation(id.getNamespace(), PureInfusionRecipe.NAME + '/' + id.getPath()));
	}

	public void save(Consumer<FinishedRecipe> consumer, String save) {
		ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result);
		if ((new ResourceLocation(save)).equals(resourcelocation)) {
			throw new IllegalStateException("Binding Recipe " + save + " should remove its 'save' argument");
		} else {
			this.save(consumer, ElementalCraft.createRL(AbstractBindingRecipe.NAME + '/' + save));
		}
	}

	public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
		consumer.accept(new Result(id, this.serializer, this.ingredients, this.result, elementAmount));
	}


	public static class Result extends AbstractFinishedRecipe {
		
		private final List<Ingredient> ingredients;
		private final Item output;
		private final int elementAmount;

		public Result(ResourceLocation id, RecipeSerializer<?> serializer, List<Ingredient> ingredients, Item result, int elementAmount) {
			super(id, serializer);
			this.ingredients = ingredients;
			this.output = result;
			this.elementAmount = elementAmount;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.addProperty(ECNames.ELEMENT_AMOUNT, elementAmount);
			JsonArray jsonarray = new JsonArray();

			for (Ingredient ingredient : this.ingredients) {
				jsonarray.add(ingredient.toJson());
			}

			json.add(ECNames.INGREDIENTS, jsonarray);
			JsonObject outputJson = new JsonObject();

			outputJson.addProperty(ECNames.ITEM, ForgeRegistries.ITEMS.getKey(this.output).toString());
			json.add(ECNames.OUTPUT, outputJson);
		}
	}
}
