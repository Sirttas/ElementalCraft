package sirttas.elementalcraft.datagen.recipe.builder;

import java.util.List;
import java.util.function.Consumer;

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
		return this.setIngredient(ElementType.NONE, Ingredient.of(tagIn));
	}

	public PureInfusionRecipeBuilder setIngredient(IItemProvider itemIn) {
		return this.setIngredient(ElementType.NONE, Ingredient.of(itemIn));
	}

	public PureInfusionRecipeBuilder setIngredient(Ingredient ingredientIn) {
		return this.setIngredient(ElementType.NONE, ingredientIn);
	}

	public PureInfusionRecipeBuilder setIngredient(ElementType type, INamedTag<Item> tagIn) {
		return this.setIngredient(type, Ingredient.of(tagIn));
	}

	public PureInfusionRecipeBuilder setIngredient(ElementType type, IItemProvider itemIn) {
		return this.setIngredient(type, Ingredient.of(itemIn));
	}

	
	public PureInfusionRecipeBuilder setIngredient(ElementType type, Ingredient ingredientIn) {
		this.ingredients.set(getIndex(type), ingredientIn);
		return this;
	}

	private int getIndex(ElementType type) {
		switch (type) {
		case NONE:
			return 0;
		case WATER:
			return 1;
		case FIRE:
			return 2;
		case EARTH:
			return 3;
		case AIR:
			return 4;
		default:
			return -1;
		}
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


	public static class Result extends AbstractFinishedRecipe {
		
		private final List<Ingredient> ingredients;
		private final Item output;
		private final int elementAmount;

		public Result(ResourceLocation id, IRecipeSerializer<?> serializer, List<Ingredient> ingredients, Item resultIn, int elementAmount) {
			super(id, serializer);
			this.ingredients = ingredients;
			this.output = resultIn;
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
