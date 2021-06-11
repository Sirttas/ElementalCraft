package sirttas.elementalcraft.datagen.recipe.builder.instrument.infusion;

import java.util.function.Consumer;

import com.google.gson.JsonObject;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.recipe.instrument.infusion.InfusionRecipe;

public class InfusionRecipeBuilder extends AbstractInfusionRecipeBuilder {
	
	private final Item result;
	protected final ElementType elementType;
	
	public InfusionRecipeBuilder(IRecipeSerializer<?> serializerIn, Ingredient ingredientIn, IItemProvider resultProviderIn, ElementType elementType) {
		super(serializerIn, ingredientIn);
		this.result = resultProviderIn.asItem();
		this.elementType = elementType;
	}

	public static InfusionRecipeBuilder infusionRecipe(Ingredient ingredientIn, IItemProvider resultIn, ElementType elementType) {
		return new InfusionRecipeBuilder(InfusionRecipe.SERIALIZER, ingredientIn, resultIn, elementType);
	}

	@Override
	protected ResourceLocation getId() {
		return ForgeRegistries.ITEMS.getKey(this.result);
	}
	
	@Override
	public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
		consumerIn.accept(new Result(id, this.serializer, this.ingredient, this.result, elementType, elementAmount));
	}

	public static class Result extends AbstractResult {
		
		private final Item output;
		protected final ElementType elementType;

		public Result(ResourceLocation idIn, IRecipeSerializer<?> serializerIn, Ingredient ingredientIn, Item resultIn, ElementType elementType, int elementAmount) {
			super(idIn, serializerIn, ingredientIn, elementAmount);
			this.output = resultIn;
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
