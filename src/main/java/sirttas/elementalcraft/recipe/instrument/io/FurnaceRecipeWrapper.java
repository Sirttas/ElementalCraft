package sirttas.elementalcraft.recipe.instrument.io;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.io.firefurnace.AbstractFireFurnaceBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.instrument.ISingleElementInstrumentRecipe;

import javax.annotation.Nonnull;

public class FurnaceRecipeWrapper<T extends AbstractCookingRecipe> implements IIOInstrumentRecipe<AbstractFireFurnaceBlockEntity<T>>, ISingleElementInstrumentRecipe<AbstractFireFurnaceBlockEntity<T>> {

	private final T recipe;

	public FurnaceRecipeWrapper(T recipe) {
		this.recipe = recipe;
	}

	@Override
	public @NotNull ItemStack assemble(@NotNull AbstractFireFurnaceBlockEntity<T> inv, @Nonnull RegistryAccess registry) {
		return recipe.assemble(inv.getInventory(), registry);
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return recipe.canCraftInDimensions(width, height);
	}

	@Nonnull
    @Override
	public ItemStack getResultItem(@Nonnull RegistryAccess registry) {
		return recipe.getResultItem(registry);
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
	public boolean matches(@Nonnull AbstractFireFurnaceBlockEntity<T> instrument, @Nonnull Level level) {
		return instrument.getContainerElementType() == ElementType.FIRE && recipe.matches(instrument.getInventory(), level);
	}
}
