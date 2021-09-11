package sirttas.elementalcraft.interaction.jei.category;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.world.item.crafting.Recipe;
import sirttas.elementalcraft.inventory.IInventoryBlockEntity;
import sirttas.elementalcraft.inventory.InventoryBlockEntityWrapper;

public abstract class AbstractBlockEntityRecipeCategory<K extends IInventoryBlockEntity, T extends Recipe<InventoryBlockEntityWrapper<K>>> extends AbstractInventoryRecipeCategory<InventoryBlockEntityWrapper<K>, T> {

	protected AbstractBlockEntityRecipeCategory(String translationKey, IDrawable icon, IDrawable background) {
		super(translationKey, icon, background);
	}
}
