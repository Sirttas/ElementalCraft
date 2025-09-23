package sirttas.elementalcraft.item.source.receptacle;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.source.trait.holder.ItemSourceTraitHolder;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.item.ECItems;

import java.util.Map;

public class ReceptacleHelper {

	private ReceptacleHelper() {}
	
	public static ElementType getElementType(ItemStack stack) {
		if (stack.is(ECItems.EMPTY_RECEPTACLE)) {
			return ElementType.NONE;
		}
		return ElementType.getElementType(stack);
	}

	public static ItemStack create(ElementType elementType) {
		return switch (elementType) {
			case FIRE -> new ItemStack(ECBlocks.FIRE_SOURCE.get());
			case WATER -> new ItemStack(ECBlocks.WATER_SOURCE.get());
			case EARTH -> new ItemStack(ECBlocks.EARTH_SOURCE.get());
			case AIR -> new ItemStack(ECBlocks.AIR_SOURCE.get());
			case NONE ->  ItemStack.EMPTY;
		};
	}

	public static ItemStack create(ElementType elementType, Map<Holder<SourceTrait>, ISourceTraitValue> traits, boolean analyzed) {
		var stack = create(elementType);

		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		}
		stack.set(ECDataComponents.SOURCE_TRAITS_HOLDER, ItemSourceTraitHolder.from(traits));
		stack.set(ECDataComponents.ELEMENT_AMOUNT, ReceptacleItem.getTraitHolder(stack).getCapacity());
		stack.set(ECDataComponents.SOURCE_ANALYZED, analyzed);
		return stack;
	}
}
