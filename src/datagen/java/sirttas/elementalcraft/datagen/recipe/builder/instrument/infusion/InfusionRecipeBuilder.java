package sirttas.elementalcraft.datagen.recipe.builder.instrument.infusion;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;

import java.util.function.Consumer;

public class InfusionRecipeBuilder extends AbstractInfusionRecipeBuilder {
	
	private final Item result;
	protected final ElementType elementType;
	
	public InfusionRecipeBuilder(RecipeSerializer<?> serializer, Ingredient ingredient, ItemLike result, ElementType elementType) {
		super(serializer, ingredient);
		this.result = result.asItem();
		this.elementType = elementType;
	}

	public static InfusionRecipeBuilder infusionRecipe(Ingredient ingredient, ItemLike result, ElementType elementType) {
		return new InfusionRecipeBuilder(ECRecipeSerializers.INFUSION.get(), ingredient, result, elementType);
	}

	@Override
	protected ResourceLocation getId() {
		return ForgeRegistries.ITEMS.getKey(this.result);
	}
	
	@Override
	public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
		consumer.accept(new Result(id, this.serializer, this.ingredient, this.result, elementType, elementAmount));
	}

	public static class Result extends AbstractResult {
		
		private final Item output;
		protected final ElementType elementType;

		public Result(ResourceLocation id, RecipeSerializer<?> serializer, Ingredient ingredientIn, Item result, ElementType elementType, int elementAmount) {
			super(id, serializer, ingredientIn, elementAmount);
			this.output = result;
			this.elementType = elementType;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			JsonObject outputJson = new JsonObject();

			outputJson.addProperty(ECNames.ITEM, ForgeRegistries.ITEMS.getKey(this.output).toString());
			json.add(ECNames.OUTPUT, outputJson);
			json.addProperty(ECNames.ELEMENT_TYPE, this.elementType.getSerializedName());
			super.serializeRecipeData(json);
		}
	}
}
