package sirttas.elementalcraft.interaction.jei.category;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import sirttas.elementalcraft.block.ECBlocks;

public abstract class AbstractInventoryRecipeCategory<I extends RecipeInput, T extends Recipe<I>> extends AbstractECRecipeCategory<T> {

	protected final ItemStack container = new ItemStack(ECBlocks.CONTAINER.get());
	
	protected AbstractInventoryRecipeCategory(String translationKey, IDrawable icon, IDrawable background) {
		super(translationKey, icon, background);
	}
}
