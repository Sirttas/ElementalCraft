package sirttas.elementalcraft.datagen.recipe.builder.instrument;

import java.util.function.Consumer;

import com.google.gson.JsonObject;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.datagen.recipe.builder.AbstractFinishedRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.AirMillGrindingRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

public class AirMillGrindingRecipeBuilder {
	
	private final Item result;
	private Ingredient ingredient;
	private int elementAmount;
	
	public AirMillGrindingRecipeBuilder(IItemProvider resultProviderIn) {
		this.result = resultProviderIn.asItem();
		elementAmount = 1000;
	}

	public static AirMillGrindingRecipeBuilder grindingRecipe(IItemProvider resultIn) {
		return new AirMillGrindingRecipeBuilder(resultIn);
	}
	
	public AirMillGrindingRecipeBuilder withElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
		return this;
	}

	public AirMillGrindingRecipeBuilder withIngredient(INamedTag<Item> tagIn) {
		return this.withIngredient(Ingredient.of(tagIn));
	}

	public AirMillGrindingRecipeBuilder withIngredient(IItemProvider itemIn) {
		return this.withIngredient(Ingredient.of(itemIn));
	}

	public AirMillGrindingRecipeBuilder withIngredient(ItemStack stack) {
		return this.withIngredient(Ingredient.of(stack));
	}
	
	public AirMillGrindingRecipeBuilder withIngredient(Ingredient ingredientIn) {
		this.ingredient = ingredientIn;
		return this;
	}
	
	public void build(Consumer<IFinishedRecipe> consumerIn) {
		ResourceLocation id = ForgeRegistries.ITEMS.getKey(this.result);

		this.build(consumerIn, ElementalCraft.createRL(IGrindingRecipe.NAME + '/' + id.getPath()));
	}

	public void build(Consumer<IFinishedRecipe> consumerIn, String save) {
		ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result);
		if ((new ResourceLocation(save)).equals(resourcelocation)) {
			throw new IllegalStateException("Grinding Recipe " + save + " should remove its 'save' argument");
		} else {
			this.build(consumerIn, ElementalCraft.createRL(IGrindingRecipe.NAME + '/' + save));
		}
	}

	public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
		consumerIn.accept(new Result(id, this.ingredient, this.result, elementAmount));
	}

	public static class Result extends AbstractFinishedRecipe {

		private final Ingredient ingredient;
		private final Item output;
		private final int elementAmount;

		public Result(ResourceLocation id, Ingredient ingredient, Item resultIn, int elementAmount) {
			super(id, AirMillGrindingRecipe.SERIALIZER);
			this.ingredient = ingredient;
			this.output = resultIn;
			this.elementAmount = elementAmount;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.addProperty(ECNames.ELEMENT_AMOUNT, elementAmount);
			json.add(ECNames.INPUT, ingredient.toJson());
			JsonObject outputJson = new JsonObject();
			
			outputJson.addProperty(ECNames.ITEM, ForgeRegistries.ITEMS.getKey(this.output).toString());
			json.add(ECNames.OUTPUT, outputJson);
		}
	}
}
