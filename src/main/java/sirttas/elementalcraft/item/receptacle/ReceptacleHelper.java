package sirttas.elementalcraft.item.receptacle;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.nbt.ECNames;

public class ReceptacleHelper {

	public static ElementType getElementType(ItemStack stack) {
		return stack.getTag() == null ? ElementType.NONE : ElementType.byName(stack.getTag().getString(ECNames.ELEMENT_TYPE));
	}

	private static ItemStack setElementType(ItemStack stack, ElementType elementType) {
		stack.getOrCreateTag().putString(ECNames.ELEMENT_TYPE, elementType.getString());
		return stack;
	}

	public static ItemStack createStack(ElementType elementType) {
		return elementType == ElementType.NONE ? new ItemStack(ECItems.emptyReceptacle) : setElementType(new ItemStack(ECItems.receptacle), elementType);
	}

	public static boolean areReceptaclesUnbreakable() {
		return ECConfig.COMMON.receptacleDurability.get() == 0;
	}

	public static ItemStack createFrom(ItemStack from, ElementType elementType) {
		ItemStack stack = ReceptacleHelper.createStack(elementType);

		if (!areReceptaclesUnbreakable()) {
			stack.setDamage(from.getDamage());
		}
		if (stack.isBookEnchantable(ItemStack.EMPTY)) {
			EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(from), stack);
		}
		return stack;
	}
}
