package sirttas.elementalcraft.recipe.instrument;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.block.instrument.purifier.TilePurifier;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.item.pureore.PureOreHelper;

public class PurifierRecipe implements IInstrumentRecipe<TilePurifier> {

	private final ItemStack ore;
	private final Ingredient input;

	public PurifierRecipe(ItemStack ore) {
		this.ore = ore;
		this.input = Ingredient.fromStacks(ore);
	}

	@Override
	public int getElementPerTick() {
		return 25;
	}

	@Override
	public int getDuration() {
		return 100;
	}

	@Override
	public boolean matches(TilePurifier inv) {
		return PureOreHelper.isValidOre(ore) && input.test(inv.getStackInSlot(0));
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.from(Ingredient.EMPTY, this.input);
	}

	@Override
	public ItemStack getRecipeOutput() {
		ItemStack result = PureOreHelper.createPureOre(ore.getItem()).copy();

		result.setCount(2);
		return result;
	}

	@Override
	public IRecipeType<?> getType() {
		return null;
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
		return null;
	}

	@Override
	public ItemStack getCraftingResult(TilePurifier inv) {
		return this.getRecipeOutput().copy();
	}

	@Override
	public void process(TilePurifier instrument) {
		ItemStack in = instrument.getStackInSlot(0);
		ItemStack output = instrument.getStackInSlot(1);
		ItemStack result = getCraftingResult(instrument);

		if (result.isItemEqual(output) && output.getCount() + result.getCount() <= output.getMaxStackSize()) {
			in.shrink(1);
			output.grow(result.getCount());
		} else if (ItemEC.isEmpty(output)) {
			in.shrink(1);
			instrument.setInventorySlotContents(1, result.copy());
		}
		if (in.isEmpty()) {
			instrument.removeStackFromSlot(0);
		}
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return null;
	}
}
