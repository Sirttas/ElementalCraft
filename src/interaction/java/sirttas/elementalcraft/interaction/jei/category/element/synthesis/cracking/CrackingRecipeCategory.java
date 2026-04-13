package sirttas.elementalcraft.interaction.jei.category.element.synthesis.cracking;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.recipe.cracking.CrackingRecipe;

import java.util.List;

public class CrackingRecipeCategory extends AbstractCrackingRecipeCategory<CrackingRecipe> {

	private static final ItemStack CRACKING_SYNTHESIZER = new ItemStack(ECBlocks.CRACKING_SYNTHESIZER.get());
	private static final List<ItemStack> CONTAINERS = List.of(
			new ItemStack(ECBlocks.SMALL_CONTAINER.get()),
			new ItemStack(ECBlocks.CONTAINER.get()),
			new ItemStack(ECBlocks.AIR_RESERVOIR.get()));

	public CrackingRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, "elementalcraft.jei.cracking", CRACKING_SYNTHESIZER, CONTAINERS);
	}

	@Override
	public @NotNull IRecipeType<@NotNull CrackingRecipe> getRecipeType() {
		return ECJEIRecipeTypes.CRACKING;
	}
}
