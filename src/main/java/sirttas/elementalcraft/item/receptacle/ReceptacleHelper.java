package sirttas.elementalcraft.item.receptacle;

import net.minecraft.item.ItemStack;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.nbt.ECNBTTags;

public class ReceptacleHelper {

	public static ElementType getElementType(ItemStack stack) {
		return stack.getTag() == null ? ElementType.NONE : ElementType.byName(stack.getTag().getString(ECNBTTags.ELEMENT_TYPE));
	}

	private static ItemStack setElementType(ItemStack stack, ElementType elementType) {
		stack.getOrCreateTag().putString(ECNBTTags.ELEMENT_TYPE, elementType.getName());
		return stack;
	}

	public static ItemStack createStack(ElementType elementType) {
		return elementType == ElementType.NONE ? new ItemStack(ECItems.emptyReceptacle) : setElementType(new ItemStack(ECItems.receptacle), elementType);
	}

	public static boolean areReceptaclesUnbreakable() {
		return ECConfig.CONFIG.receptacleDurability.get() == 0;
	}
}
