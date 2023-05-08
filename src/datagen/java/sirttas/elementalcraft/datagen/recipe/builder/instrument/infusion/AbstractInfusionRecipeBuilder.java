package sirttas.elementalcraft.datagen.recipe.builder.instrument.infusion;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.datagen.recipe.builder.AbstractFinishedRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;

import java.util.function.Consumer;

public abstract class AbstractInfusionRecipeBuilder {

	protected final Ingredient ingredient;
	protected int elementAmount;
	protected final RecipeSerializer<?> serializer;

	protected AbstractInfusionRecipeBuilder(RecipeSerializer<?> serializer, Ingredient ingredient) {
		this.serializer = serializer;
		this.ingredient = ingredient;
		elementAmount = 1000;
	}

	public AbstractInfusionRecipeBuilder withElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
		return this;
	}

	protected abstract ResourceLocation getId();
	
	public void save(Consumer<FinishedRecipe> consumer) {
		ResourceLocation id = getId();

		this.save(consumer, new ResourceLocation(id.getNamespace(), IInfusionRecipe.NAME + '/' + id.getPath()));
	}

	public void save(Consumer<FinishedRecipe> consumerIn, String save) {
		ResourceLocation resourcelocation = getId();
		if ((new ResourceLocation(save)).equals(resourcelocation)) {
			throw new IllegalStateException("Infusion Recipe " + save + " should remove its 'save' argument");
		} else {
			this.save(consumerIn, ElementalCraft.createRL(IInfusionRecipe.NAME + '/' + save));
		}
	}

	public abstract void save(Consumer<FinishedRecipe> consumer, ResourceLocation id);
	
	public abstract static class AbstractResult extends AbstractFinishedRecipe {
		
		private final Ingredient ingredient;
		private final int elementAmount;

		protected AbstractResult(ResourceLocation id, RecipeSerializer<?> serializer, Ingredient ingredient, int elementAmount) {
			super(id, serializer);
			this.ingredient = ingredient;
			this.elementAmount = elementAmount;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.addProperty(ECNames.ELEMENT_AMOUNT, elementAmount);
			json.add(ECNames.INPUT, this.ingredient.toJson());
		}
	}
}
