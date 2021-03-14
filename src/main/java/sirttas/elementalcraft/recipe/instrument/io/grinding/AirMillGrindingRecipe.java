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
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.recipe.RecipeHelper;

public class AirMillGrindingRecipe extends AbstractGrindingRecipe {
	
	@ObjectHolder(ElementalCraft.MODID + ":" + NAME) public static final IRecipeSerializer<AirMillGrindingRecipe> SERIALIZER = null;
	
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
		return NonNullList.from(Ingredient.EMPTY, ingredient);
	}

	@Override
	public ItemStack getRecipeOutput() {
		return output.copy();
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<AirMillGrindingRecipe> {

		@Override
		public AirMillGrindingRecipe read(ResourceLocation recipeId, JsonObject json) {
			int elementAmount = JSONUtils.getInt(json, ECNames.ELEMENT_AMOUNT);
			Ingredient ingredient = RecipeHelper.deserializeIngredient(json, ECNames.INPUT);
			ItemStack output = RecipeHelper.readRecipeOutput(json, ECNames.OUTPUT);

			if (!output.isEmpty()) {
				return new AirMillGrindingRecipe(recipeId, ingredient, output, elementAmount);
			}
			return null;
		}

		@Override
		public AirMillGrindingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			int elementAmount = buffer.readInt();
			Ingredient ingredient = Ingredient.read(buffer);
			ItemStack output = buffer.readItemStack();

			return new AirMillGrindingRecipe(recipeId, ingredient, output, elementAmount);
		}

		@Override
		public void write(PacketBuffer buffer, AirMillGrindingRecipe recipe) {
			buffer.writeInt(recipe.getElementAmount());
			recipe.getIngredients().get(0).write(buffer);
			buffer.writeItemStack(recipe.getRecipeOutput());
		}
		
	}
	
}
