package sirttas.elementalcraft.item.elemental;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.item.ECItemStackHelper;

public class DamageableCraftingElementalItem extends ElementalItem {


	public DamageableCraftingElementalItem(ElementType elementType, Properties properties) {
		super(elementType, properties);
	}

	@Override
	public boolean hasCraftingRemainingItem(@NotNull ItemStack stack) {
		return ECItemStackHelper.canBeDamaged(stack);
	}

	@Override
	public @NotNull ItemStack getCraftingRemainingItem(@NotNull ItemStack stack) {
		return ECItemStackHelper.damageItem(stack);
	}

}
