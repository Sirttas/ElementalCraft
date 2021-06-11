package sirttas.elementalcraft.recipe.instrument.io;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.firefurnace.AbstractFireFurnaceBlockEntity;
import sirttas.elementalcraft.config.ECConfig;

public class FurnaceRecipeWrapper<T extends AbstractCookingRecipe> implements IIOInstrumentRecipe<AbstractFireFurnaceBlockEntity<T>> {

	private final T recipe;

	public FurnaceRecipeWrapper(T recipe) {
		this.recipe = recipe;
	}

	@Override
	public ItemStack getCraftingResult(AbstractFireFurnaceBlockEntity<T> inv) {
		return recipe.assemble(inv.getInventory());
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return recipe.canCraftInDimensions(width, height);
	}

	@Override
	public ItemStack getResultItem() {
		return recipe.getResultItem();
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
	public void process(AbstractFireFurnaceBlockEntity<T> instrument) {
		IIOInstrumentRecipe.super.process(instrument);
		instrument.addExperience(recipe.getExperience());
	}

	@Override
	public ElementType getElementType() {
		return ElementType.FIRE;
	}

	public int getDuration() {
		return recipe.getCookingTime();
	}

	@Override
	public int getElementAmount() {
		return getDuration() * (recipe.getType() == IRecipeType.SMELTING ? ECConfig.COMMON.fireFurnaceElementAmount.get() : ECConfig.COMMON.fireBlastFurnaceElementAmount.get());
	}

	@Override
	public boolean matches(AbstractFireFurnaceBlockEntity<T> instrument) {
		return instrument.getTankElementType() == ElementType.FIRE && recipe.matches(instrument.getInventory(), instrument.getLevel());
	}
}
