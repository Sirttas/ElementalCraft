package sirttas.elementalcraft.recipe.instrument.binding;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.recipe.input.MultipleItemsSingleElementRecipeInput;
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
	public boolean matches(MultipleItemsSingleElementRecipeInput input, @NotNull Level level) {
		return input.size() == 1 && recipe.matches(input.singleItem(), level);
	}
	
	@Override
	public @NotNull ItemStack assemble(@NotNull MultipleItemsSingleElementRecipeInput input, @Nonnull HolderLookup.Provider provider) {
		if (input.size() == 1) {
			return recipe.assemble(input.singleItem(), provider);
		}
		return super.assemble(input, provider);
	}

	@Nonnull
	@Override
	public ItemStack getResultItem(@Nonnull HolderLookup.Provider provider) {
		return recipe.getResultItem(provider);
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return recipe.getSerializer();
	}
}
