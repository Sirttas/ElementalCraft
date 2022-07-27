package sirttas.elementalcraft.datagen.recipe.builder.instrument;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.datagen.recipe.builder.AbstractFinishedRecipe;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

import java.util.function.Consumer;

public class AirMillGrindingRecipeBuilder {
	
	private final Item result;
	private Ingredient ingredient;
	private int elementAmount;
	private int luckRatio;
	private int count;

	public AirMillGrindingRecipeBuilder(ItemLike resultProviderIn) {
		this.result = resultProviderIn.asItem();
		elementAmount = 1000;
		luckRatio = 0;
		count = 1;
	}

	public static AirMillGrindingRecipeBuilder grindingRecipe(ItemLike resultIn) {
		return new AirMillGrindingRecipeBuilder(resultIn);
	}
	
	public AirMillGrindingRecipeBuilder withElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
		return this;
	}

	public AirMillGrindingRecipeBuilder withIngredient(TagKey<Item> tag) {
		return this.withIngredient(Ingredient.of(tag));
	}

	public AirMillGrindingRecipeBuilder withIngredient(ItemLike itemIn) {
		return this.withIngredient(Ingredient.of(itemIn));
	}

	public AirMillGrindingRecipeBuilder withIngredient(ItemStack stack) {
		return this.withIngredient(Ingredient.of(stack));
	}
	
	public AirMillGrindingRecipeBuilder withIngredient(Ingredient ingredientIn) {
		this.ingredient = ingredientIn;
		return this;
	}

	public AirMillGrindingRecipeBuilder withLuckRatio(int luckRatio) {
		this.luckRatio = luckRatio;
		return this;
	}

	public AirMillGrindingRecipeBuilder withCount(int count) {
		this.count = count;
		return this;
	}

	public void build(Consumer<FinishedRecipe> consumerIn) {
		ResourceLocation id = ForgeRegistries.ITEMS.getKey(this.result);

		this.build(consumerIn, ElementalCraft.createRL(IGrindingRecipe.NAME + '/' + id.getPath()));
	}

	public void build(Consumer<FinishedRecipe> consumerIn, String save) {
		ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result);
		if ((new ResourceLocation(save)).equals(resourcelocation)) {
			throw new IllegalStateException("Grinding Recipe " + save + " should remove its 'save' argument");
		} else {
			this.build(consumerIn, ElementalCraft.createRL(IGrindingRecipe.NAME + '/' + save));
		}
	}

	public void build(Consumer<FinishedRecipe> consumerIn, ResourceLocation id) {
		consumerIn.accept(new Result(id, this.ingredient, this.result, elementAmount, luckRatio, count));
	}

	public static class Result extends AbstractFinishedRecipe {

		private final Ingredient ingredient;
		private final Item output;
		private final int elementAmount;
		private final int luckRatio;
		private final int count;

		public Result(ResourceLocation id, Ingredient ingredient, Item resultIn, int elementAmount, int luckRatio, int count) {
			super(id, ECRecipeSerializers.AIR_MILL_GRINDING.get());
			this.ingredient = ingredient;
			this.output = resultIn;
			this.elementAmount = elementAmount;
			this.luckRatio = luckRatio;
			this.count = count;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.addProperty(ECNames.ELEMENT_AMOUNT, elementAmount);
			json.add(ECNames.INPUT, ingredient.toJson());
			json.addProperty(ECNames.LUCK_RATION, luckRatio);
			JsonObject outputJson = new JsonObject();

			outputJson.addProperty(ECNames.ITEM, ForgeRegistries.ITEMS.getKey(this.output).toString());
			if (this.count > 1) {
				outputJson.addProperty("count", this.count);
			}
			json.add(ECNames.OUTPUT, outputJson);
		}
	}
}
