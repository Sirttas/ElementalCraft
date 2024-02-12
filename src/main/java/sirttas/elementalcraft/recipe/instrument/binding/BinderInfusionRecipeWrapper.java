package sirttas.elementalcraft.recipe.instrument.binding;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.instrument.binder.IBinder;
import sirttas.elementalcraft.block.instrument.infuser.IInfuser;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;

import javax.annotation.Nonnull;

public class BinderInfusionRecipeWrapper extends AbstractBindingRecipe {

	private final IInfusionRecipe recipe;
	
	public BinderInfusionRecipeWrapper(IInfusionRecipe infusionRecipe) {
		super(infusionRecipe.getElementType());
		this.recipe = infusionRecipe;
	}

	@Override
	public int getElementAmount() {
		return recipe.getElementAmount();
	}

	@Override
	public boolean matches(IBinder inv, @NotNull Level level) {
		return inv instanceof IInfuser infuser && recipe.matches(infuser, level);
	}
	
	@Override
	public @NotNull ItemStack assemble(@NotNull IBinder instrument, @Nonnull RegistryAccess registry) {
		if (instrument instanceof IInfuser infuser) {
			return recipe.assemble(infuser, registry);
		}
		return super.assemble(instrument, registry);
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
}
