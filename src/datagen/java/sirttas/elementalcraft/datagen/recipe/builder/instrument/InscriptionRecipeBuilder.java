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
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.instrument.InscriptionRecipe;

import java.util.List;
import java.util.function.Consumer;

public class InscriptionRecipeBuilder {

	private final ResourceLocation output;
	private final List<Ingredient> ingredients = Lists.newArrayList();
	private final ElementType elementType;
	private int elementAmount;
	private final RecipeSerializer<?> serializer;
	private Ingredient slate;

	public InscriptionRecipeBuilder(RecipeSerializer<?> serializerIn, ResourceLocation output, ElementType elementType) {
		this.serializer = serializerIn;
		this.elementType = elementType;
		elementAmount = 5000;
		this.output = output;
	}

	public static InscriptionRecipeBuilder inscriptionRecipe(ResourceLocation output, ElementType elementType) {
		return new InscriptionRecipeBuilder(ECRecipeSerializers.INSCRIPTION.get(), output, elementType);
	}

	public InscriptionRecipeBuilder withElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
		return this;
	}

	public InscriptionRecipeBuilder setSlate(TagKey<Item> tagIn) {
		return this.setSlate(Ingredient.of(tagIn));
	}

	public InscriptionRecipeBuilder setSlate(ItemLike itemIn) {
		return this.setSlate(Ingredient.of(itemIn));
	}

	public InscriptionRecipeBuilder setSlate(Ingredient ingredientIn) {
		slate = ingredientIn;
		return this;
	}

	public InscriptionRecipeBuilder addIngredient(TagKey<Item> tagIn) {
		return this.addIngredient(Ingredient.of(tagIn));
	}

	public InscriptionRecipeBuilder addIngredient(ItemLike itemIn) {
		return this.addIngredient(Ingredient.of(itemIn));
	}

	public InscriptionRecipeBuilder addIngredient(Ingredient ingredientIn) {
		this.ingredients.add(ingredientIn);
		return this;
	}

	public void build(Consumer<FinishedRecipe> consumerIn) {
		this.build(consumerIn, output.getPath());
	}

	public void build(Consumer<FinishedRecipe> consumerIn, String save) {
		this.build(consumerIn, ElementalCraft.createRL(InscriptionRecipe.NAME + '/' + save));
	}

	public void build(Consumer<FinishedRecipe> consumerIn, ResourceLocation id) {
		consumerIn.accept(new Result(id, this.serializer, this.slate, this.ingredients, this.output, elementType, elementAmount));
	}

	public static class Result extends AbstractFinishedRecipe {
		private final Ingredient slate;
		private final List<Ingredient> ingredients;
		private final ResourceLocation output;
		private final ElementType elementType;
		private final int elementAmount;

		public Result(ResourceLocation id, RecipeSerializer<?> serializer, Ingredient slate, List<Ingredient> ingredients, ResourceLocation output, ElementType elementType, int elementAmount) {
			super(id, serializer);
			this.slate = slate;
			this.ingredients = ingredients;
			this.output = output;
			this.elementType = elementType;
			this.elementAmount = elementAmount;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.addProperty(ECNames.ELEMENT_TYPE, this.elementType.getSerializedName());
			json.addProperty(ECNames.ELEMENT_AMOUNT, elementAmount);
			json.add("slate", slate.toJson());
			JsonArray jsonarray = new JsonArray();

			for (Ingredient ingredient : this.ingredients) {
				jsonarray.add(ingredient.toJson());
			}

			json.add(ECNames.INGREDIENTS, jsonarray);
			json.add(ECNames.OUTPUT, getJsonOutput());
		}

		private JsonObject getJsonOutput() {
			JsonObject json = new JsonObject();
			JsonObject tagJson = new JsonObject();
			JsonObject ecNbtJson = new JsonObject();
			
			json.addProperty(ECNames.ITEM, ForgeRegistries.ITEMS.getKey(ECItems.RUNE.get()).toString());
			ecNbtJson.addProperty(ECNames.RUNE, this.output.toString());
			tagJson.add(ECNames.EC_NBT, ecNbtJson);
			json.add(ECNames.NBT, tagJson);
			return json;
		}
	}
}
