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
import sirttas.elementalcraft.recipe.instrument.io.sawing.SawingRecipe;

import java.util.function.Consumer;

public class SawingRecipeBuilder {

	private final Item result;
	private Ingredient ingredient;
	private int elementAmount;
	private int luckRatio;
	private int count;

	public SawingRecipeBuilder(ItemLike result) {
		this.result = result.asItem();
		elementAmount = 1000;
		luckRatio = 0;
		count = 1;
	}

	public static SawingRecipeBuilder sawingRecipe(ItemLike result) {
		return new SawingRecipeBuilder(result);
	}
	
	public SawingRecipeBuilder withElementAmount(int elementAmount) {
		this.elementAmount = elementAmount;
		return this;
	}

	public SawingRecipeBuilder withIngredient(TagKey<Item> tag) {
		return this.withIngredient(Ingredient.of(tag));
	}

	public SawingRecipeBuilder withIngredient(ItemLike item) {
		return this.withIngredient(Ingredient.of(item));
	}

	public SawingRecipeBuilder withIngredient(ItemStack stack) {
		return this.withIngredient(Ingredient.of(stack));
	}
	
	public SawingRecipeBuilder withIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
		return this;
	}

	public SawingRecipeBuilder withLuckRatio(int luckRatio) {
		this.luckRatio = luckRatio;
		return this;
	}

	public SawingRecipeBuilder withCount(int count) {
		this.count = count;
		return this;
	}

	public void save(Consumer<FinishedRecipe> consumer) {
		ResourceLocation id = ForgeRegistries.ITEMS.getKey(this.result);

		this.save(consumer, ElementalCraft.createRL(SawingRecipe.NAME + '/' + id.getPath()));
	}

	public void save(Consumer<FinishedRecipe> consumer, String save) {
		ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result);
		if ((new ResourceLocation(save)).equals(resourcelocation)) {
			throw new IllegalStateException("Sawing Recipe " + save + " should remove its 'save' argument");
		} else {
			this.save(consumer, ElementalCraft.createRL(SawingRecipe.NAME + '/' + save));
		}
	}

	public void save(Consumer<FinishedRecipe> consumerIn, ResourceLocation id) {
		consumerIn.accept(new Result(id, this.ingredient, this.result, elementAmount, luckRatio, count));
	}

	public static class Result extends AbstractFinishedRecipe {

		private final Ingredient ingredient;
		private final Item output;
		private final int elementAmount;
		private final int luckRatio;
		private final int count;

		public Result(ResourceLocation id, Ingredient ingredient, Item resultIn, int elementAmount, int luckRatio, int count) {
			super(id, ECRecipeSerializers.SAWING.get());
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
