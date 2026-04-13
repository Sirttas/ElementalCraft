package sirttas.elementalcraft.interaction.jei.category.element.synthesis.cracking;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.recipe.cracking.SculkCrackingRecipe;

import java.util.List;

public class SculkCrackingRecipeCategory extends AbstractCrackingRecipeCategory<SculkCrackingRecipe> {

	private static final ItemStack SCULK_CRACKING_SYNTHESIZER = new ItemStack(ECBlocks.SCULK_CRACKING_SYNTHESIZER.get());
	private static final List<ItemStack> CONTAINERS = List.of(
			new ItemStack(ECBlocks.CONTAINER.get()),
			new ItemStack(ECBlocks.AIR_RESERVOIR.get()));

	public SculkCrackingRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, "elementalcraft.jei.sculk_cracking", SCULK_CRACKING_SYNTHESIZER, CONTAINERS);
	}

	@Override
	public @NotNull IRecipeType<@NotNull SculkCrackingRecipe> getRecipeType() {
		return ECJEIRecipeTypes.SCULK_CRACKING;
	}
}
