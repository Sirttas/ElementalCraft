package sirttas.elementalcraft.recipe.instrument.infusion;

import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.instrument.infuser.IInfuser;
import sirttas.elementalcraft.recipe.RecipeHelper;
import sirttas.elementalcraft.recipe.instrument.AbstractInstrumentRecipe;

public class InfusionRecipe extends AbstractInstrumentRecipe<IInfuser> implements IInfusionRecipe {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + NAME) public static final IRecipeSerializer<InfusionRecipe> SERIALIZER = null;

	private final Ingredient input;
	private final ItemStack output;
	private final int elementAmount;

	public InfusionRecipe(ResourceLocation id, ElementType type, int elementAmount, ItemStack output, Ingredient input) {
		super(id, type);
		this.input = input;
		this.output = output;
		this.elementAmount = elementAmount;
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}
	
	@Override
	public Ingredient getInput() {
		return input;
	}

	@Override
	public ItemStack getResultItem() {
		return output;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<InfusionRecipe> {

		@Override
		public InfusionRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			ElementType type = ElementType.byName(JSONUtils.getAsString(json, ECNames.ELEMENT_TYPE));
			int elementAmount = JSONUtils.getAsInt(json, ECNames.ELEMENT_AMOUNT);
			Ingredient input = RecipeHelper.deserializeIngredient(json, ECNames.INPUT);
			ItemStack output = RecipeHelper.readRecipeOutput(json, ECNames.OUTPUT);

			return new InfusionRecipe(recipeId, type, elementAmount, output, input);
		}

		@Override
		public InfusionRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
			ElementType type = ElementType.byName(buffer.readUtf());
			int elementAmount = buffer.readInt();
			Ingredient input = Ingredient.fromNetwork(buffer);
			ItemStack output = buffer.readItem();

			return new InfusionRecipe(recipeId, type, elementAmount, output, input);
		}

		@Override
		public void toNetwork(PacketBuffer buffer, InfusionRecipe recipe) {
			buffer.writeUtf(recipe.getElementType().getSerializedName());
			buffer.writeInt(recipe.getElementAmount());
			recipe.getInput().toNetwork(buffer);
			buffer.writeItem(recipe.getResultItem());
		}
	}
}
