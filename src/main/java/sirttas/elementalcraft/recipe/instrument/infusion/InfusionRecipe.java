package sirttas.elementalcraft.recipe.instrument.infusion;

import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.infuser.TileInfuser;
import sirttas.elementalcraft.nbt.ECNames;
import sirttas.elementalcraft.recipe.RecipeHelper;

public class InfusionRecipe extends AbstractInfusionRecipe {

	public static final String NAME = "infusion";
	@ObjectHolder(ElementalCraft.MODID + ":" + NAME) public static IRecipeSerializer<InfusionRecipe> SERIALIZER;

	private Ingredient input;
	private ItemStack output;
	private int elementPerTick;
	private int duration;

	public InfusionRecipe(ResourceLocation id, ElementType type, int elementPerTick, int duration, ItemStack output, Ingredient input) {
		super(id, type);
		this.input = input;
		this.output = output;
		this.elementPerTick = elementPerTick;
		this.duration = duration;
	}

	@Override
	public int getElementPerTick() {
		return elementPerTick;
	}

	@Override
	public int getDuration() {
		return duration;
	}

	@Override
	public boolean matches(TileInfuser inv) {
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

	public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<InfusionRecipe> {
		final IRecipeFactory factory;

		public Serializer(IRecipeFactory factory) {
			this.factory = factory;
		}

		@Override
		public InfusionRecipe read(ResourceLocation recipeId, JsonObject json) {
			ElementType type = ElementType.byName(JSONUtils.getString(json, "element"));
			int elementPerTick = JSONUtils.getInt(json, "consumption", 10);
			int duration = JSONUtils.getInt(json, "duration", 100);
			Ingredient input = RecipeHelper.deserializeIngredient(json, ECNames.INPUT);
			ItemStack output = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getString(json, ECNames.OUTPUT))));

			return this.factory.create(recipeId, type, elementPerTick, duration, output, input);
		}

		@Override
		public InfusionRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			ElementType type = ElementType.byName(buffer.readString(32767));
			int elementPerTick = buffer.readInt();
			int duration = buffer.readInt();
			Ingredient input = Ingredient.read(buffer);
			ItemStack output = buffer.readItemStack();

			return this.factory.create(recipeId, type, elementPerTick, duration, output, input);
		}

		@Override
		public void write(PacketBuffer buffer, InfusionRecipe recipe) {
			buffer.writeString(recipe.getElementType().getName());
			buffer.writeInt(recipe.getElementPerTick());
			buffer.writeInt(recipe.getDuration());
			recipe.getIngredients().get(0).write(buffer);
			buffer.writeItemStack(recipe.getRecipeOutput());
		}

		public interface IRecipeFactory {
			InfusionRecipe create(ResourceLocation id, ElementType type, int elementPerTick, int duration, ItemStack output, Ingredient input);
		}
	}
}
