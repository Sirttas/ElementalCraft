package sirttas.elementalcraft.item.receptacle;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.tag.ECTags;

public class ReceptacleHelper {

	private ReceptacleHelper() {}
	
	public static ElementType getElementType(ItemStack stack) {
		return stack.getTag() == null ? ElementType.NONE : ElementType.byName(stack.getTag().getString(ECNames.ELEMENT_TYPE));
	}

	private static ItemStack setElementType(ItemStack stack, ElementType elementType) {
		stack.getOrCreateTag().putString(ECNames.ELEMENT_TYPE, elementType.getString());
		return stack;
	}

	public static ItemStack create(ElementType elementType) {
		return elementType == ElementType.NONE ? new ItemStack(ECItems.EMPTY_RECEPTACLE) : setElementType(new ItemStack(ECItems.RECEPTACLE), elementType);
	}

	public static ItemStack createImproved(ElementType elementType) {
		return elementType == ElementType.NONE ? new ItemStack(ECItems.EMPTY_RECEPTACLE_IMPROVED) : setElementType(new ItemStack(ECItems.RECEPTACLE_IMPROVED), elementType);
	}

	public static ItemStack createFrom(ItemStack from, ElementType elementType) {
		ItemStack stack = ECTags.Items.IMPROVED_RECEPTACLES.contains(from.getItem()) ? ReceptacleHelper.createImproved(elementType) : ReceptacleHelper.create(elementType);

		if (stack.isDamageable()) {
			stack.setDamage(from.getDamage());
		}
		if (stack.isBookEnchantable(ItemStack.EMPTY)) {
			EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(from), stack);
		}
		return stack;
	}
}
