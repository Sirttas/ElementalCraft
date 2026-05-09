package sirttas.elementalcraft.recipe.instrument.binding;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.recipe.RuntimeRecipe;
import sirttas.elementalcraft.recipe.input.MultipleItemsSingleElementRecipeInput;
import sirttas.elementalcraft.recipe.instrument.infusion.InfusionRecipe;

import java.util.List;

public class BinderInfusionRecipeWrapper extends AbstractBindingRecipe implements RuntimeRecipe<MultipleItemsSingleElementRecipeInput> {

	private final InfusionRecipe recipe;
	
	public BinderInfusionRecipeWrapper(InfusionRecipe infusionRecipe) {
		super(new CommonInfo(false), infusionRecipe.getElementType(), infusionRecipe.getElementAmount());
		this.recipe = infusionRecipe;
	}

	@Override
	public boolean matches(MultipleItemsSingleElementRecipeInput input, Level level) {
		return input.size() == 1 && recipe.matches(input.singleItem(), level);
	}
	
	@Override
	public ItemStack assemble(MultipleItemsSingleElementRecipeInput input) {
		if (input.size() == 1) {
			return recipe.assemble(input.singleItem());
		}
		return ItemStack.EMPTY;
	}

    @Override
    public PlacementInfo placementInfo() {
        return recipe.placementInfo();
    }

	@Override
	public List<RecipeDisplay> display() {
		return recipe.display();
	}
}
