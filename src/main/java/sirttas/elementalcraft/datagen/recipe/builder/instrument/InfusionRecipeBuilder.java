package sirttas.elementalcraft.datagen.recipe.builder.instrument;

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
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.recipe.instrument.infusion.AbstractInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.InfusionRecipe;

public class InfusionRecipeBuilder {
	private final Item result;
	private final Ingredient ingredient;
	private final ElementType elementType;
	private int elementAmount;
	private final IRecipeSerializer<?> serializer;

	public InfusionRecipeBuilder(IRecipeSerializer<?> serializerIn, Ingredient ingredientIn, IItemProvider resultProviderIn, ElementType elementType) {
		this.serializer = serializerIn;
		this.result = resultProviderIn.asItem();
		this.ingredient = ingredientIn;
		this.elementType = elementType;
		elementAmount = 1000;
	}

	public static InfusionRecipeBuilder infusionRecipe(Ingredient ingredientIn, IItemProvider resultIn, ElementType elementType) {
		return new InfusionRecipeBuilder(InfusionRecipe.SERIALIZER, ingredientIn, resultIn, elementType);
	}

	public InfusionRecipeBuilder withElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
		return this;
	}

	public void build(Consumer<IFinishedRecipe> consumerIn) {
		ResourceLocation id = ForgeRegistries.ITEMS.getKey(this.result);

		this.build(consumerIn, new ResourceLocation(id.getNamespace(), AbstractInfusionRecipe.NAME + '/' + id.getPath()));
	}

	public void build(Consumer<IFinishedRecipe> consumerIn, String save) {
		ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result);
		if ((new ResourceLocation(save)).equals(resourcelocation)) {
			throw new IllegalStateException("Infusion Recipe " + save + " should remove its 'save' argument");
		} else {
			this.build(consumerIn, ElementalCraft.createRL(AbstractInfusionRecipe.NAME + '/' + save));
		}
	}

	public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
		consumerIn.accept(new Result(id, this.serializer, this.ingredient, this.result, elementType, elementAmount));
	}


	public static class Result implements IFinishedRecipe {
		private final ResourceLocation id;
		private final Ingredient ingredient;
		private final Item output;
		private final ElementType elementType;
		private final int elementAmount;
		private final IRecipeSerializer<?> serializer;

		public Result(ResourceLocation idIn, IRecipeSerializer<?> serializerIn, Ingredient ingredientIn, Item resultIn, ElementType elementType, int elementAmount) {
			this.id = idIn;
			this.serializer = serializerIn;
			this.ingredient = ingredientIn;
			this.output = resultIn;
			this.elementType = elementType;
			this.elementAmount = elementAmount;
		}

		@Override
		public void serialize(JsonObject json) {
			json.addProperty(ECNames.ELEMENT_TYPE, this.elementType.getString());
			json.addProperty(ECNames.ELEMENT_AMOUNT, elementAmount);
			json.add(ECNames.INPUT, this.ingredient.serialize());
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
