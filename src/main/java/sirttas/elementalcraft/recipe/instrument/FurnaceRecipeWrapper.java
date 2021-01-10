package sirttas.elementalcraft.recipe.instrument;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.firefurnace.AbstractTileFireFurnace;
import sirttas.elementalcraft.config.ECConfig;

public class FurnaceRecipeWrapper<T extends AbstractCookingRecipe> implements IInstrumentRecipe<AbstractTileFireFurnace<T>> {

	private T recipe;

	public FurnaceRecipeWrapper(T recipe) {
		this.recipe = recipe;
	}

	@Override
	public ItemStack getCraftingResult(AbstractTileFireFurnace<T> inv) {
		return recipe.getCraftingResult(inv.getInventory());
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
		IInventory inv = instrument.getInventory();
		ItemStack input = inv.getStackInSlot(0);
		ItemStack output = inv.getStackInSlot(1);
		ItemStack result = recipe.getCraftingResult(inv);

		if (result.isItemEqual(output) && output.getCount() + result.getCount() <= output.getMaxStackSize()) {
			input.shrink(1);
			output.grow(result.getCount());
		} else if (output.isEmpty()) {
			input.shrink(1);
			inv.setInventorySlotContents(1, result.copy());
		}
		if (input.isEmpty()) {
			inv.removeStackFromSlot(0);
		}
		instrument.addExperience(recipe.getExperience());
	}

	@Override
	public ElementType getElementType() {
		return ElementType.FIRE;
	}

	public int getDuration() {
		return recipe.getCookTime();
	}

	@Override
	public int getElementAmount() {
		return getDuration() * (recipe.getType() == IRecipeType.SMELTING ? ECConfig.COMMON.fireFurnaceElementAmount.get() : ECConfig.COMMON.fireBlastFurnaceElementAmount.get());
	}

	@Override
	public boolean matches(AbstractTileFireFurnace<T> instrument) {
		return instrument.getTankElementType() == ElementType.FIRE && recipe.matches(instrument.getInventory(), instrument.getWorld());
	}
}
