package sirttas.elementalcraft.interaction.jei.category;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.world.item.crafting.Recipe;
import sirttas.elementalcraft.container.IContainerBlockEntity;
import sirttas.elementalcraft.container.ContainerBlockEntityWrapper;

public abstract class AbstractBlockEntityRecipeCategory<K extends IContainerBlockEntity, T extends Recipe<ContainerBlockEntityWrapper<K>>> extends AbstractInventoryRecipeCategory<ContainerBlockEntityWrapper<K>, T> {

	protected AbstractBlockEntityRecipeCategory(String translationKey, IDrawable icon, IDrawable background) {
		super(translationKey, icon, background);
	}
}
