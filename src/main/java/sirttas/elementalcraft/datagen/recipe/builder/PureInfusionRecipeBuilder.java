package sirttas.elementalcraft.datagen.recipe.builder;

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
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.recipe.PureInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;

public class PureInfusionRecipeBuilder {
	private final Item result;
	private final List<Ingredient> ingredients = Lists.newArrayList(Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY);
	private int elementAmount;
	private final IRecipeSerializer<?> serializer;

	public PureInfusionRecipeBuilder(IRecipeSerializer<?> serializerIn, IItemProvider resultProviderIn) {
		this.serializer = serializerIn;
		this.result = resultProviderIn.asItem();
		elementAmount = 60000;
	}

	public static PureInfusionRecipeBuilder pureInfusionRecipe(IItemProvider resultIn) {
		return new PureInfusionRecipeBuilder(PureInfusionRecipe.SERIALIZER, resultIn);
	}

	public PureInfusionRecipeBuilder withElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
		return this;
	}

	public PureInfusionRecipeBuilder setIngredient(INamedTag<Item> tagIn) {
		return this.setIngredient(ElementType.NONE, Ingredient.fromTag(tagIn));
	}

	public PureInfusionRecipeBuilder setIngredient(IItemProvider itemIn) {
		return this.setIngredient(ElementType.NONE, Ingredient.fromItems(itemIn));
	}

	public PureInfusionRecipeBuilder setIngredient(Ingredient ingredientIn) {
		return this.setIngredient(ElementType.NONE, ingredientIn);
	}

	public PureInfusionRecipeBuilder setIngredient(ElementType type, INamedTag<Item> tagIn) {
		return this.setIngredient(type, Ingredient.fromTag(tagIn));
	}

	public PureInfusionRecipeBuilder setIngredient(ElementType type, IItemProvider itemIn) {
		return this.setIngredient(type, Ingredient.fromItems(itemIn));
	}

	public PureInfusionRecipeBuilder setIngredient(ElementType type, Ingredient ingredientIn) {
		int index = type == ElementType.NONE ? 0 : 
					type == ElementType.WATER ? 1 :
					type == ElementType.FIRE ? 2 :
					type == ElementType.EARTH ? 3 :
					type == ElementType.AIR ? 4 :
					-1;

		this.ingredients.set(index, ingredientIn);
		return this;
	}

	public void build(Consumer<IFinishedRecipe> consumerIn) {
		ResourceLocation id = ForgeRegistries.ITEMS.getKey(this.result);

		this.build(consumerIn, new ResourceLocation(id.getNamespace(), PureInfusionRecipe.NAME + '/' + id.getPath()));
	}

	public void build(Consumer<IFinishedRecipe> consumerIn, String save) {
		ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result);
		if ((new ResourceLocation(save)).equals(resourcelocation)) {
			throw new IllegalStateException("Binding Recipe " + save + " should remove its 'save' argument");
		} else {
			this.build(consumerIn, ElementalCraft.createRL(AbstractBindingRecipe.NAME + '/' + save));
		}
	}

	public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
		consumerIn.accept(new Result(id, this.serializer, this.ingredients, this.result, elementAmount));
	}


	public static class Result implements IFinishedRecipe {
		private final ResourceLocation id;
		private final List<Ingredient> ingredients;
		private final Item output;
		private final int elementAmount;
		private final IRecipeSerializer<?> serializer;

		public Result(ResourceLocation idIn, IRecipeSerializer<?> serializerIn, List<Ingredient> ingredients, Item resultIn, int elementAmount) {
			this.id = idIn;
			this.serializer = serializerIn;
			this.ingredients = ingredients;
			this.output = resultIn;
			this.elementAmount = elementAmount;
		}

		@Override
		public void serialize(JsonObject json) {
			json.addProperty(ECNames.ELEMENT_AMOUNT, elementAmount);
			JsonArray jsonarray = new JsonArray();

			for (Ingredient ingredient : this.ingredients) {
				jsonarray.add(ingredient.serialize());
			}

			json.add(ECNames.INGREDIENTS, jsonarray);
			JsonObject outputJson = new JsonObject();

			outputJson.addProperty(ECNames.ITEM, ForgeRegistries.ITEMS.getKey(this.output).toString());
			json.add(ECNames.OUTPUT, outputJson);
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
