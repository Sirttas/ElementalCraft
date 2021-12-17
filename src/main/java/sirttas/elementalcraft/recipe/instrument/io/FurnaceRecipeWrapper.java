package sirttas.elementalcraft.recipe.instrument.io;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.io.firefurnace.AbstractFireFurnaceBlockEntity;
import sirttas.elementalcraft.config.ECConfig;

import javax.annotation.Nonnull;

public class FurnaceRecipeWrapper<T extends AbstractCookingRecipe> implements IIOInstrumentRecipe<AbstractFireFurnaceBlockEntity<T>> {

	private final T recipe;

	public FurnaceRecipeWrapper(T recipe) {
		this.recipe = recipe;
	}

	@Override
	public ItemStack assemble(AbstractFireFurnaceBlockEntity<T> inv) {
		return recipe.assemble(inv.getInventory());
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return recipe.canCraftInDimensions(width, height);
	}

	@Nonnull
    @Override
	public ItemStack getResultItem() {
		return recipe.getResultItem();
	}

	@Nonnull
    @Override
	public ResourceLocation getId() {
		return recipe.getId();
	}

	@Nonnull
    @Override
	public RecipeSerializer<?> getSerializer() {
		return recipe.getSerializer();
	}

	@Nonnull
    @Override
	public RecipeType<?> getType() {
		return recipe.getType();
	}
	
	public float getExperience() {
		return recipe.getExperience();
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
		return getDuration() * (recipe.getType() == RecipeType.SMELTING ? ECConfig.COMMON.fireFurnaceElementAmount.get() : ECConfig.COMMON.fireBlastFurnaceElementAmount.get());
	}

	@Override
	public boolean matches(AbstractFireFurnaceBlockEntity<T> instrument) {
		return instrument.getContainerElementType() == ElementType.FIRE && recipe.matches(instrument.getInventory(), instrument.getLevel());
	}
}
