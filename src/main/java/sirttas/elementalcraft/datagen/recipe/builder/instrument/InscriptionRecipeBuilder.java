package sirttas.elementalcraft.datagen.recipe.builder.instrument;

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
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.instrument.InscriptionRecipe;

public class InscriptionRecipeBuilder {

	private final ResourceLocation output;
	private final List<Ingredient> ingredients = Lists.newArrayList();
	private final ElementType elementType;
	private int elementAmount;
	private final IRecipeSerializer<?> serializer;
	private Ingredient slate;

	public InscriptionRecipeBuilder(IRecipeSerializer<?> serializerIn, ResourceLocation output, ElementType elementType) {
		this.serializer = serializerIn;
		this.elementType = elementType;
		elementAmount = 5000;
		this.output = output;
	}

	public static InscriptionRecipeBuilder inscriptionRecipe(ResourceLocation output, ElementType elementType) {
		return new InscriptionRecipeBuilder(InscriptionRecipe.SERIALIZER, output, elementType);
	}

	public InscriptionRecipeBuilder withElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
		return this;
	}

	public InscriptionRecipeBuilder setSlate(INamedTag<Item> tagIn) {
		return this.setSlate(Ingredient.fromTag(tagIn));
	}

	public InscriptionRecipeBuilder setSlate(IItemProvider itemIn) {
		return this.setSlate(Ingredient.fromItems(itemIn));
	}

	public InscriptionRecipeBuilder setSlate(Ingredient ingredientIn) {
		slate = ingredientIn;
		return this;
	}

	public InscriptionRecipeBuilder addIngredient(INamedTag<Item> tagIn) {
		return this.addIngredient(Ingredient.fromTag(tagIn));
	}

	public InscriptionRecipeBuilder addIngredient(IItemProvider itemIn) {
		return this.addIngredient(Ingredient.fromItems(itemIn));
	}

	public InscriptionRecipeBuilder addIngredient(Ingredient ingredientIn) {
		this.ingredients.add(ingredientIn);
		return this;
	}

	public void build(Consumer<IFinishedRecipe> consumerIn) {
		this.build(consumerIn, output.getPath());
	}

	public void build(Consumer<IFinishedRecipe> consumerIn, String save) {
		this.build(consumerIn, ElementalCraft.createRL(InscriptionRecipe.NAME + '/' + save));
	}

	public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
		consumerIn.accept(new Result(id, this.serializer, this.slate, this.ingredients, this.output, elementType, elementAmount));
	}

	public static class Result implements IFinishedRecipe {
		private final ResourceLocation id;
		private final Ingredient slate;
		private final List<Ingredient> ingredients;
		private final ResourceLocation output;
		private final ElementType elementType;
		private final int elementAmount;
		private final IRecipeSerializer<?> serializer;

		public Result(ResourceLocation idIn, IRecipeSerializer<?> serializerIn, Ingredient slate, List<Ingredient> ingredients, ResourceLocation output, ElementType elementType, int elementAmount) {
			this.id = idIn;
			this.serializer = serializerIn;
			this.slate = slate;
			this.ingredients = ingredients;
			this.output = output;
			this.elementType = elementType;
			this.elementAmount = elementAmount;
		}

		@Override
		public void serialize(JsonObject json) {
			json.addProperty(ECNames.ELEMENT_TYPE, this.elementType.getString());
			json.addProperty(ECNames.ELEMENT_AMOUNT, elementAmount);
			json.add("slate", slate.serialize());
			JsonArray jsonarray = new JsonArray();

			for (Ingredient ingredient : this.ingredients) {
				jsonarray.add(ingredient.serialize());
			}

			json.add(ECNames.INGREDIENTS, jsonarray);
			json.add(ECNames.OUTPUT, getJsonOutput());
		}

		private JsonObject getJsonOutput() {
			JsonObject json = new JsonObject();
			JsonObject tagJson = new JsonObject();
			JsonObject ecNbtJson = new JsonObject();
			
			json.addProperty(ECNames.ITEM, ForgeRegistries.ITEMS.getKey(ECItems.RUNE).toString());
			ecNbtJson.addProperty(ECNames.RUNE, this.output.toString());
			tagJson.add(ECNames.EC_NBT, ecNbtJson);
			json.add(ECNames.NBT, tagJson);
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
