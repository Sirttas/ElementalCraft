package sirttas.elementalcraft.recipe.instrument.infusion;

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
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.instrument.infuser.IInfuser;
import sirttas.elementalcraft.recipe.RecipeHelper;

public class InfusionRecipe extends AbstractInfusionRecipe {

	@ObjectHolder(ElementalCraft.MODID + ":" + NAME) public static final IRecipeSerializer<InfusionRecipe> SERIALIZER = null;

	private Ingredient input;
	private ItemStack output;
	private int elementAmount;

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
	public boolean matches(IInfuser inv) {
		ItemStack stack = inv.getItem();

		if (super.matches(inv) && inv.getTankElementType() == getElementType()) {
			return input.test(stack);
		}
		return false;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return output;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.from(Ingredient.EMPTY, this.input);
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<InfusionRecipe> {

		@Override
		public InfusionRecipe read(ResourceLocation recipeId, JsonObject json) {
			ElementType type = ElementType.byName(JSONUtils.getString(json, ECNames.ELEMENT_TYPE));
			int elementAmount = JSONUtils.getInt(json, ECNames.ELEMENT_AMOUNT);
			Ingredient input = RecipeHelper.deserializeIngredient(json, ECNames.INPUT);
			ItemStack output = RecipeHelper.readRecipeOutput(json, ECNames.OUTPUT);

			return new InfusionRecipe(recipeId, type, elementAmount, output, input);
		}

		@Override
		public InfusionRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			ElementType type = ElementType.byName(buffer.readString(32767));
			int elementAmount = buffer.readInt();
			Ingredient input = Ingredient.read(buffer);
			ItemStack output = buffer.readItemStack();

			return new InfusionRecipe(recipeId, type, elementAmount, output, input);
		}

		@Override
		public void write(PacketBuffer buffer, InfusionRecipe recipe) {
			buffer.writeString(recipe.getElementType().getString());
			buffer.writeInt(recipe.getElementAmount());
			recipe.getIngredients().get(0).write(buffer);
			buffer.writeItemStack(recipe.getRecipeOutput());
		}
	}
}
