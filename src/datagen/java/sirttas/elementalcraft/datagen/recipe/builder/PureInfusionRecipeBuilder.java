package sirttas.elementalcraft.datagen.recipe.builder;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag.Named;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.recipe.PureInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;

import java.util.List;
import java.util.function.Consumer;

public class PureInfusionRecipeBuilder {
	private final Item result;
	private final List<Ingredient> ingredients = Lists.newArrayList(Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY, Ingredient.EMPTY);
	private int elementAmount;
	private final RecipeSerializer<?> serializer;

	public PureInfusionRecipeBuilder(RecipeSerializer<?> serializerIn, ItemLike resultProviderIn) {
		this.serializer = serializerIn;
		this.result = resultProviderIn.asItem();
		elementAmount = 60000;
	}

	public static PureInfusionRecipeBuilder pureInfusionRecipe(ItemLike resultIn) {
		return new PureInfusionRecipeBuilder(PureInfusionRecipe.SERIALIZER, resultIn);
	}

	public PureInfusionRecipeBuilder withElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
		return this;
	}

	public PureInfusionRecipeBuilder setIngredient(Named<Item> tagIn) {
		return this.setIngredient(ElementType.NONE, Ingredient.of(tagIn));
	}

	public PureInfusionRecipeBuilder setIngredient(ItemLike itemIn) {
		return this.setIngredient(ElementType.NONE, Ingredient.of(itemIn));
	}

	public PureInfusionRecipeBuilder setIngredient(Ingredient ingredientIn) {
		return this.setIngredient(ElementType.NONE, ingredientIn);
	}

	public PureInfusionRecipeBuilder setIngredient(ElementType type, Named<Item> tagIn) {
		return this.setIngredient(type, Ingredient.of(tagIn));
	}

	public PureInfusionRecipeBuilder setIngredient(ElementType type, ItemLike itemIn) {
		return this.setIngredient(type, Ingredient.of(itemIn));
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
	
	public void build(Consumer<FinishedRecipe> consumerIn) {
		ResourceLocation id = ForgeRegistries.ITEMS.getKey(this.result);

		this.build(consumerIn, new ResourceLocation(id.getNamespace(), PureInfusionRecipe.NAME + '/' + id.getPath()));
	}

	public void build(Consumer<FinishedRecipe> consumerIn, String save) {
		ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result);
		if ((new ResourceLocation(save)).equals(resourcelocation)) {
			throw new IllegalStateException("Binding Recipe " + save + " should remove its 'save' argument");
		} else {
			this.build(consumerIn, ElementalCraft.createRL(AbstractBindingRecipe.NAME + '/' + save));
		}
	}

	public void build(Consumer<FinishedRecipe> consumerIn, ResourceLocation id) {
		consumerIn.accept(new Result(id, this.serializer, this.ingredients, this.result, elementAmount));
	}


	public static class Result extends AbstractFinishedRecipe {
		
		private final List<Ingredient> ingredients;
		private final Item output;
		private final int elementAmount;

		public Result(ResourceLocation id, RecipeSerializer<?> serializer, List<Ingredient> ingredients, Item resultIn, int elementAmount) {
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
