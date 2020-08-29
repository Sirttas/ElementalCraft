package sirttas.elementalcraft.data.recipe.builder;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.recipe.instrument.infusion.InfusionRecipe;

public class InfusionRecipeBuilder {
	private final Item result;
	private final Ingredient ingredient;
	private final ElementType elementType;
	private int consumption;
	private int duration;
	private final IRecipeSerializer<?> serializer;

	public InfusionRecipeBuilder(IRecipeSerializer<?> serializerIn, Ingredient ingredientIn, IItemProvider resultProviderIn, ElementType elementType) {
		this.serializer = serializerIn;
		this.result = resultProviderIn.asItem();
		this.ingredient = ingredientIn;
		this.elementType = elementType;
		consumption = 0;
		duration = 0;
	}

	public static InfusionRecipeBuilder infusionRecipe(Ingredient ingredientIn, IItemProvider resultIn, ElementType elementType) {
		return new InfusionRecipeBuilder(InfusionRecipe.SERIALIZER, ingredientIn, resultIn, elementType);
	}

	public InfusionRecipeBuilder withConsumption(int consumption) {
		this.consumption = consumption;
		return this;
	}

	public InfusionRecipeBuilder withDuration(int duration) {
		this.duration = duration;
		return this;
	}

	public void build(Consumer<IFinishedRecipe> consumerIn) {
		this.build(consumerIn, ForgeRegistries.ITEMS.getKey(this.result));
	}

	public void build(Consumer<IFinishedRecipe> consumerIn, String save) {
		ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result);
		if ((new ResourceLocation(save)).equals(resourcelocation)) {
			throw new IllegalStateException("Infusion Recipe " + save + " should remove its 'save' argument");
		} else {
			this.build(consumerIn, new ResourceLocation(save));
		}
	}

	public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
		consumerIn.accept(new Result(id, this.serializer, this.ingredient, this.result, elementType, consumption, duration));
	}


	public static class Result implements IFinishedRecipe {
		private final ResourceLocation id;
		private final Ingredient ingredient;
		private final Item output;
		private final ElementType elementType;
		private final int consumption;
		private final int duration;
		private final IRecipeSerializer<?> serializer;

		public Result(ResourceLocation idIn, IRecipeSerializer<?> serializerIn, Ingredient ingredientIn, Item resultIn, ElementType elementType, int consumption, int duration) {
			this.id = idIn;
			this.serializer = serializerIn;
			this.ingredient = ingredientIn;
			this.output = resultIn;
			this.elementType = elementType;
			this.consumption = consumption;
			this.duration = duration;
		}

		@Override
		public void serialize(JsonObject json) {
			json.addProperty("element", this.elementType.getString());
			if (consumption > 0) {
				json.addProperty("consumption", consumption);
			}
			if (duration > 0) {
				json.addProperty("duration", duration);
			}
			json.add("input", this.ingredient.serialize());
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
