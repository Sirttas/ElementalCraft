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
import sirttas.elementalcraft.nbt.ECNames;
import sirttas.elementalcraft.recipe.PureInfusionRecipe;

public class PureInfusionRecipeBuilder {
	private final Item result;
	private final List<Ingredient> ingredients = Lists.newArrayList(Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY);
	private int consumption;
	private int duration;
	private final IRecipeSerializer<?> serializer;

	public PureInfusionRecipeBuilder(IRecipeSerializer<?> serializerIn, IItemProvider resultProviderIn) {
		this.serializer = serializerIn;
		this.result = resultProviderIn.asItem();
		consumption = 0;
		duration = 0;
	}

	public static PureInfusionRecipeBuilder pureInfusionRecipe(IItemProvider resultIn) {
		return new PureInfusionRecipeBuilder(PureInfusionRecipe.SERIALIZER, resultIn);
	}

	public PureInfusionRecipeBuilder withConsumption(int consumption) {
		this.consumption = consumption;
		return this;
	}

	public PureInfusionRecipeBuilder withDuration(int duration) {
		this.duration = duration;
		return this;
	}

	public PureInfusionRecipeBuilder setIngredient(Tag<Item> tagIn) {
		return this.setIngredient(ElementType.NONE, Ingredient.fromTag(tagIn));
	}

	public PureInfusionRecipeBuilder setIngredient(IItemProvider itemIn) {
		return this.setIngredient(ElementType.NONE, Ingredient.fromItems(itemIn));
	}

	public PureInfusionRecipeBuilder setIngredient(Ingredient ingredientIn) {
		return this.setIngredient(ElementType.NONE, ingredientIn);
	}

	public PureInfusionRecipeBuilder setIngredient(ElementType type, Tag<Item> tagIn) {
		return this.setIngredient(type, Ingredient.fromTag(tagIn));
	}

	public PureInfusionRecipeBuilder setIngredient(ElementType type, IItemProvider itemIn) {
		return this.setIngredient(type, Ingredient.fromItems(itemIn));
	}

	public PureInfusionRecipeBuilder setIngredient(ElementType type, Ingredient ingredientIn) {
		int index = type == ElementType.NONE ? 0 : 
					type == ElementType.WATER ? 1 : // NOSONAR
					type == ElementType.FIRE ? 2 : // NOSONAR
					type == ElementType.EARTH ? 3 : // NOSONAR
					type == ElementType.AIR ? 4 : // NOSONAR
					-1; // NOSONAR

		this.ingredients.set(index, ingredientIn);
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
		consumerIn.accept(new Result(id, this.serializer, this.ingredients, this.result, consumption, duration));
	}


	public static class Result implements IFinishedRecipe {
		private final ResourceLocation id;
		private final List<Ingredient> ingredients;
		private final Item output;
		private final int consumption;
		private final int duration;
		private final IRecipeSerializer<?> serializer;

		public Result(ResourceLocation idIn, IRecipeSerializer<?> serializerIn, List<Ingredient> ingredients, Item resultIn, int consumption, int duration) {
			this.id = idIn;
			this.serializer = serializerIn;
			this.ingredients = ingredients;
			this.output = resultIn;
			this.consumption = consumption;
			this.duration = duration;
		}

		@Override
		public void serialize(JsonObject json) {
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
			json.addProperty(ECNames.OUTPUT, ForgeRegistries.ITEMS.getKey(this.output).toString());
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
