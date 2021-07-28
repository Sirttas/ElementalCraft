package sirttas.elementalcraft.datagen.recipe.builder.instrument.infusion;

import java.util.function.Consumer;

import com.google.gson.JsonObject;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.datagen.recipe.builder.AbstractFinishedRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;

public abstract class AbstractInfusionRecipeBuilder {

	protected final Ingredient ingredient;
	protected int elementAmount;
	protected final RecipeSerializer<?> serializer;

	protected AbstractInfusionRecipeBuilder(RecipeSerializer<?> serializerIn, Ingredient ingredientIn) {
		this.serializer = serializerIn;
		this.ingredient = ingredientIn;
		elementAmount = 1000;
	}

	public AbstractInfusionRecipeBuilder withElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
		return this;
	}

	protected abstract ResourceLocation getId();
	
	public void build(Consumer<FinishedRecipe> consumerIn) {
		ResourceLocation id = getId();

		this.build(consumerIn, new ResourceLocation(id.getNamespace(), IInfusionRecipe.NAME + '/' + id.getPath()));
	}

	public void build(Consumer<FinishedRecipe> consumerIn, String save) {
		ResourceLocation resourcelocation = getId();
		if ((new ResourceLocation(save)).equals(resourcelocation)) {
			throw new IllegalStateException("Infusion Recipe " + save + " should remove its 'save' argument");
		} else {
			this.build(consumerIn, ElementalCraft.createRL(IInfusionRecipe.NAME + '/' + save));
		}
	}

	public abstract void build(Consumer<FinishedRecipe> consumerIn, ResourceLocation id);
	
	public abstract static class AbstractResult extends AbstractFinishedRecipe {
		
		private final Ingredient ingredient;
		private final int elementAmount;

		protected AbstractResult(ResourceLocation id, RecipeSerializer<?> serializer, Ingredient ingredientIn, int elementAmount) {
			super(id, serializer);
			this.ingredient = ingredientIn;
			this.elementAmount = elementAmount;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.addProperty(ECNames.ELEMENT_AMOUNT, elementAmount);
			json.add(ECNames.INPUT, this.ingredient.toJson());
		}
	}
}