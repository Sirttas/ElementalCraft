package sirttas.elementalcraft.recipe.instrument;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.block.instrument.firefurnace.AbstractTileFireFurnace;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.ItemEC;

public class FurnaceRecipeWrapper<T extends AbstractCookingRecipe> implements IInstrumentRecipe<AbstractTileFireFurnace<T>> {

	private T recipe;

	public FurnaceRecipeWrapper(T recipe) {
		this.recipe = recipe;
	}

	@Override
	public ItemStack getCraftingResult(AbstractTileFireFurnace<T> inv) {
		return recipe.getCraftingResult(inv);
	}

	@Override
	public boolean canFit(int width, int height) {
		return recipe.canFit(width, height);
	}

	@Override
	public ItemStack getRecipeOutput() {
		return recipe.getRecipeOutput();
	}

	@Override
	public ResourceLocation getId() {
		return recipe.getId();
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return recipe.getSerializer();
	}

	@Override
	public IRecipeType<?> getType() {
		return recipe.getType();
	}

	@Override
	public void process(AbstractTileFireFurnace<T> instrument) {
		ItemStack input = instrument.getStackInSlot(0);
		ItemStack output = instrument.getStackInSlot(1);
		ItemStack result = recipe.getCraftingResult(instrument);

		if (result.isItemEqual(output) && output.getCount() + result.getCount() <= output.getMaxStackSize()) {
			input.shrink(1);
			output.grow(result.getCount());
		} else if (ItemEC.isEmpty(output)) {
			input.shrink(1);
			instrument.setInventorySlotContents(1, result.copy());
		}
		if (input.isEmpty()) {
			instrument.removeStackFromSlot(0);
		}
		instrument.addExperience(recipe.getExperience());
	}

	@Override
	public ElementType getElementType() {
		return ElementType.FIRE;
	}

	@Override
	public int getElementPerTick() {
		return ECConfig.CONFIG.fireFurnaceConsumeAmount.get();
	}

	@Override
	public int getDuration() {
		return recipe.getCookTime();
	}

	@Override
	public boolean matches(AbstractTileFireFurnace<T> inv) {
		return inv.getTankElementType() == ElementType.FIRE && recipe.matches(inv, null);
	}

}
