package sirttas.elementalcraft.datagen.recipe.builder.instrument;

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
import sirttas.elementalcraft.datagen.recipe.builder.AbstractFinishedRecipe;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;

import java.util.List;
import java.util.function.Consumer;

public class BindingRecipeBuilder {
	private final Item result;
	private final List<Ingredient> ingredients = Lists.newArrayList();
	private final ElementType elementType;
	private int elementAmount;
	private final RecipeSerializer<?> serializer;

	public BindingRecipeBuilder(RecipeSerializer<?> serializer, ItemLike result, ElementType elementType) {
		this.serializer = serializer;
		this.result = result.asItem();
		this.elementType = elementType;
		elementAmount = 2500;
	}

	public static BindingRecipeBuilder bindingRecipe(ItemLike result, ElementType elementType) {
		return new BindingRecipeBuilder(ECRecipeSerializers.BINDING.get(), result, elementType);
	}

	public BindingRecipeBuilder withElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
		return this;
	}

	public BindingRecipeBuilder addIngredient(TagKey<Item> tag) {
		return this.addIngredient(Ingredient.of(tag));
	}

	public BindingRecipeBuilder addIngredient(ItemLike item) {
		return this.addIngredient(Ingredient.of(item));
	}

	public BindingRecipeBuilder addIngredient(Ingredient ingredient) {
		this.ingredients.add(ingredient);
		return this;
	}


	public void save(Consumer<FinishedRecipe> consumer) {
		ResourceLocation id = ForgeRegistries.ITEMS.getKey(this.result);

		this.save(consumer, new ResourceLocation(id.getNamespace(), AbstractBindingRecipe.NAME + '/' + id.getPath()));
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
		consumer.accept(new Result(id, this.serializer, this.ingredients, this.result, elementType, elementAmount));
	}


	public static class Result extends AbstractFinishedRecipe {
		private final List<Ingredient> ingredients;
		private final Item output;
		private final ElementType elementType;
		private final int elementAmount;

		public Result(ResourceLocation id, RecipeSerializer<?> serializer, List<Ingredient> ingredients, Item result, ElementType elementType, int elementAmount) {
			super(id, serializer);
			this.ingredients = ingredients;
			this.output = result;
			this.elementType = elementType;
			this.elementAmount = elementAmount;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.addProperty(ECNames.ELEMENT_TYPE, this.elementType.getSerializedName());
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
