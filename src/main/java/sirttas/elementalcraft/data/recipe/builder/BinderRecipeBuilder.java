package sirttas.elementalcraft.data.recipe.builder;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.recipe.instrument.BinderRecipe;

public class BinderRecipeBuilder {
	private final Item result;
	private final List<Ingredient> ingredients = Lists.newArrayList();
	private final ElementType elementType;
	private int consumption;
	private int duration;
	private final IRecipeSerializer<?> serializer;

	public BinderRecipeBuilder(IRecipeSerializer<?> serializerIn, IItemProvider resultProviderIn, ElementType elementType) {
		this.serializer = serializerIn;
		this.result = resultProviderIn.asItem();
		this.elementType = elementType;
		consumption = 0;
		duration = 0;
	}

	public static BinderRecipeBuilder binderRecipe(IItemProvider resultIn, ElementType elementType) {
		return new BinderRecipeBuilder(BinderRecipe.SERIALIZER, resultIn, elementType);
	}

	public BinderRecipeBuilder withConsumption(int consumption) {
		this.consumption = consumption;
		return this;
	}

	public BinderRecipeBuilder withDuration(int duration) {
		this.duration = duration;
		return this;
	}

	public BinderRecipeBuilder addIngredient(Tag<Item> tagIn) {
		return this.addIngredient(Ingredient.fromTag(tagIn));
	}

	public BinderRecipeBuilder addIngredient(IItemProvider itemIn) {
		return this.addIngredient(Ingredient.fromItems(itemIn));
	}

	public BinderRecipeBuilder addIngredient(Ingredient ingredientIn) {
		this.ingredients.add(ingredientIn);
		return this;
	}


	public void build(Consumer<IFinishedRecipe> consumerIn) {
		this.build(consumerIn, ForgeRegistries.ITEMS.getKey(this.result));
	}

	public void build(Consumer<IFinishedRecipe> consumerIn, String save) {
		ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result);
		if ((new ResourceLocation(save)).equals(resourcelocation)) {
			throw new IllegalStateException("Binding Recipe " + save + " should remove its 'save' argument");
		} else {
			this.build(consumerIn, new ResourceLocation(save));
		}
	}

	public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
		consumerIn.accept(new Result(id, this.serializer, this.ingredients, this.result, elementType, consumption, duration));
	}


	public static class Result implements IFinishedRecipe {
		private final ResourceLocation id;
		private final List<Ingredient> ingredients;
		private final Item output;
		private final ElementType elementType;
		private final int consumption;
		private final int duration;
		private final IRecipeSerializer<?> serializer;

		public Result(ResourceLocation idIn, IRecipeSerializer<?> serializerIn, List<Ingredient> ingredients, Item resultIn, ElementType elementType, int consumption, int duration) {
			this.id = idIn;
			this.serializer = serializerIn;
			this.ingredients = ingredients;
			this.output = resultIn;
			this.elementType = elementType;
			this.consumption = consumption;
			this.duration = duration;
		}

		@Override
		public void serialize(JsonObject json) {
			json.addProperty("element", this.elementType.func_176610_l/* getName */());
			if (consumption > 0) {
				json.addProperty("consumption", consumption);
			}
			if (duration > 0) {
				json.addProperty("duration", duration);
			}
			JsonArray jsonarray = new JsonArray();

			for (Ingredient ingredient : this.ingredients) {
				jsonarray.add(ingredient.serialize());
			}

			json.add("ingredients", jsonarray);
			json.addProperty("output", ForgeRegistries.ITEMS.getKey(this.output).toString());
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
