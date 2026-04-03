package sirttas.elementalcraft.recipe.instrument.binding;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.recipe.RuntimeRecipe;
import sirttas.elementalcraft.recipe.input.MultipleItemsSingleElementRecipeInput;
import sirttas.elementalcraft.recipe.instrument.infusion.InfusionRecipe;

public class BinderInfusionRecipeWrapper extends AbstractBindingRecipe implements RuntimeRecipe<MultipleItemsSingleElementRecipeInput> {

	private final InfusionRecipe recipe;
	
	public BinderInfusionRecipeWrapper(InfusionRecipe infusionRecipe) {
		super(null, infusionRecipe.getElementType(), infusionRecipe.getElementAmount());
		this.recipe = infusionRecipe;
	}

	@Override
	public boolean matches(MultipleItemsSingleElementRecipeInput input, @NotNull Level level) {
		return input.size() == 1 && recipe.matches(input.singleItem(), level);
	}
	
	@Override
	public @NotNull ItemStack assemble(@NotNull MultipleItemsSingleElementRecipeInput input) {
		if (input.size() == 1) {
			return recipe.assemble(input.singleItem());
		}
		return ItemStack.EMPTY;
	}

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return recipe.placementInfo();
    }
}
