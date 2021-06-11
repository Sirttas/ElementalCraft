package sirttas.elementalcraft.recipe.instrument.io.grinding;

import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.recipe.RecipeHelper;

public class AirMillGrindingRecipe implements IGrindingRecipe {
	
	@ObjectHolder(ElementalCraftApi.MODID + ":" + NAME) public static final IRecipeSerializer<AirMillGrindingRecipe> SERIALIZER = null;
	
	private final Ingredient ingredient;
	private final ItemStack output;
	private final int elementAmount;
	private final ResourceLocation id;
	
	protected AirMillGrindingRecipe(ResourceLocation id, Ingredient ingredient, ItemStack output, int elementAmount) {
		this.id = id;
		this.ingredient = ingredient;
		this.output = output;
		this.elementAmount = elementAmount;
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}
	
	@Override
	public boolean matches(ItemStack stack) {
		return ingredient.test(stack);
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(Ingredient.EMPTY, ingredient);
	}

	@Override
	public ItemStack getResultItem() {
		return output;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<AirMillGrindingRecipe> {

		@Override
		public AirMillGrindingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			int elementAmount = JSONUtils.getAsInt(json, ECNames.ELEMENT_AMOUNT);
			Ingredient ingredient = RecipeHelper.deserializeIngredient(json, ECNames.INPUT);
			ItemStack output = RecipeHelper.readRecipeOutput(json, ECNames.OUTPUT);

			if (!output.isEmpty()) {
				return new AirMillGrindingRecipe(recipeId, ingredient, output, elementAmount);
			}
			return null;
		}

		@Override
		public AirMillGrindingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
			int elementAmount = buffer.readInt();
			Ingredient ingredient = Ingredient.fromNetwork(buffer);
			ItemStack output = buffer.readItem();

			return new AirMillGrindingRecipe(recipeId, ingredient, output, elementAmount);
		}

		@Override
		public void toNetwork(PacketBuffer buffer, AirMillGrindingRecipe recipe) {
			buffer.writeInt(recipe.getElementAmount());
			recipe.getIngredients().get(0).toNetwork(buffer);
			buffer.writeItem(recipe.getResultItem());
		}
		
	}
	
}
