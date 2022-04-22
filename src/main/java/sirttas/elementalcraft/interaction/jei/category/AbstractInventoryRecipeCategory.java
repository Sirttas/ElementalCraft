package sirttas.elementalcraft.interaction.jei.category;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import sirttas.elementalcraft.item.ECItems;

public abstract class AbstractInventoryRecipeCategory<I extends Container, T extends Recipe<I>> extends AbstractECRecipeCategory<T> {

	protected final ItemStack tank = new ItemStack(ECItems.TANK);
	
	protected AbstractInventoryRecipeCategory(String translationKey, IDrawable icon, IDrawable background) {
		super(translationKey, icon, background);
	}

	protected int getGaugeValue(int amount) {
		return (int) Math.log10(amount) - 1;
	}
}
