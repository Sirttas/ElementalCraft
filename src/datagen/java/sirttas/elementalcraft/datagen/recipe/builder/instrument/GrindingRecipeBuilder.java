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

public class GrindingRecipeBuilder {
	
	private final Item result;
	private Ingredient ingredient;
	private int elementAmount;
	private int luckRatio;
	private int count;

	public GrindingRecipeBuilder(ItemLike result) {
		this.result = result.asItem();
		elementAmount = 1000;
		luckRatio = 0;
		count = 1;
	}

	public static GrindingRecipeBuilder grindingRecipe(ItemLike result) {
		return new GrindingRecipeBuilder(result);
	}
	
	public GrindingRecipeBuilder withElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
		return this;
	}

	public GrindingRecipeBuilder withIngredient(TagKey<Item> tag) {
		return this.withIngredient(Ingredient.of(tag));
	}

	public GrindingRecipeBuilder withIngredient(ItemLike item) {
		return this.withIngredient(Ingredient.of(item));
	}

	public GrindingRecipeBuilder withIngredient(ItemStack stack) {
		return this.withIngredient(Ingredient.of(stack));
	}
	
	public GrindingRecipeBuilder withIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
		return this;
	}

	public GrindingRecipeBuilder withLuckRatio(int luckRatio) {
		this.luckRatio = luckRatio;
		return this;
	}

	public GrindingRecipeBuilder withCount(int count) {
		this.count = count;
		return this;
	}

	public void save(Consumer<FinishedRecipe> consumer) {
		ResourceLocation id = ForgeRegistries.ITEMS.getKey(this.result);

		this.save(consumer, ElementalCraft.createRL(IGrindingRecipe.NAME + '/' + id.getPath()));
	}

	public void save(Consumer<FinishedRecipe> consumer, String save) {
		ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result);
		if ((new ResourceLocation(save)).equals(resourcelocation)) {
			throw new IllegalStateException("Grinding Recipe " + save + " should remove its 'save' argument");
		} else {
			this.save(consumer, ElementalCraft.createRL(IGrindingRecipe.NAME + '/' + save));
		}
	}

	public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
		consumer.accept(new Result(id, this.ingredient, this.result, elementAmount, luckRatio, count));
	}

	public static class Result extends AbstractFinishedRecipe {

		private final Ingredient ingredient;
		private final Item output;
		private final int elementAmount;
		private final int luckRatio;
		private final int count;

		public Result(ResourceLocation id, Ingredient ingredient, Item result, int elementAmount, int luckRatio, int count) {
			super(id, ECRecipeSerializers.GRINDING.get());
			this.ingredient = ingredient;
			this.output = result;
			this.elementAmount = elementAmount;
			this.luckRatio = luckRatio;
			this.count = count;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.addProperty(ECNames.ELEMENT_AMOUNT, elementAmount);
			json.add(ECNames.INPUT, ingredient.toJson());
			json.addProperty(ECNames.LUCK_RATIO, luckRatio);
			JsonObject outputJson = new JsonObject();

			outputJson.addProperty(ECNames.ITEM, ForgeRegistries.ITEMS.getKey(this.output).toString());
			if (this.count > 1) {
				outputJson.addProperty("count", this.count);
			}
			json.add(ECNames.OUTPUT, outputJson);
		}
	}
}
