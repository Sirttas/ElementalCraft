package sirttas.elementalcraft.recipe.instrument.purification;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.purifier.TilePurifier;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

public class PurifierRecipe implements IInstrumentRecipe<TilePurifier> {

	public static final String NAME = "purification";
	public static final IRecipeType<PurifierRecipe> TYPE = Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(NAME), new IRecipeType<PurifierRecipe>() {
		@Override
		public String toString() {
			return NAME;
		}
	});
	@ObjectHolder(ElementalCraft.MODID + ":" + NAME) public static IRecipeSerializer<PurifierRecipe> SERIALIZER;

	private Ingredient input;
	private ItemStack output;
	private int elementPerTick;
	private int duration;
	private ResourceLocation id;

	public PurifierRecipe(ResourceLocation id, int elementPerTick, int duration, ItemStack output, Ingredient input) {
		this.id = id;
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
	public boolean matches(TilePurifier inv) {
		return input.test(inv.getStackInSlot(0));
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.from(Ingredient.EMPTY, this.input);
	}

	@Override
	public ItemStack getRecipeOutput() {
		return output;
	}

	@Override
	public IRecipeType<?> getType() {
		return TYPE;
	}

	@Override
	public ElementType getElementType() {
		return ElementType.EARTH;
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public ItemStack getCraftingResult(TilePurifier inv) {
		return this.getRecipeOutput().copy();
	}

	@Override
	public void process(TilePurifier instrument) {
		instrument.getStackInSlot(0).shrink(1);
		instrument.setInventorySlotContents(1, this.getCraftingResult(instrument));
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<PurifierRecipe> {
		final IRecipeFactory factory;

		public Serializer(IRecipeFactory factory) {
			this.factory = factory;
		}

		@Override
		public PurifierRecipe read(ResourceLocation recipeId, JsonObject json) {
			throw new RuntimeException("Purifier recipes are generated at runtime");
		}

		@Override
		public PurifierRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			int elementPerTick = buffer.readInt();
			int duration = buffer.readInt();
			Ingredient input = Ingredient.read(buffer);
			ItemStack output = buffer.readItemStack();

			return this.factory.create(recipeId, elementPerTick, duration, output, input);
		}

		@Override
		public void write(PacketBuffer buffer, PurifierRecipe recipe) {
			buffer.writeInt(recipe.getElementPerTick());
			buffer.writeInt(recipe.getDuration());
			recipe.getIngredients().get(0).write(buffer);
			buffer.writeItemStack(recipe.getRecipeOutput());
		}

		public interface IRecipeFactory {
			PurifierRecipe create(ResourceLocation id, int elementPerTick, int duration, ItemStack output, Ingredient input);
		}
	}
}
