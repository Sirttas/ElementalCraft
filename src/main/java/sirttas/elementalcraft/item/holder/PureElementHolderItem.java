package sirttas.elementalcraft.item.holder;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.element.ElementAmounts;

public class PureElementHolderItem extends AbstractElementHolderItem {

	public static final String NAME = "pure_element_holder";

	public PureElementHolderItem(Item.Properties properties) {
		super(ECConfig.SERVER.pureElementHolderCapacity::get, ECConfig.SERVER.pureElementHolderTransferAmount::get, properties);
	}

	@Override
	public IElementStorage getElementStorage(ItemStack stack) {
		return new ElementStorage(stack);
	}

	@Override
	protected ElementType getElementType(IElementStorage target, BlockState blockstate) {
		if (target instanceof IElementTypeProvider provider) {
			return provider.getElementType();
		}
		return ElementType.getElementType(blockstate);
	}
	
	private class ElementStorage implements IElementStorage {

		private final ItemStack stack;
		
		public ElementStorage(ItemStack stack) {
			this.stack = stack;
		}

		@Override
		public boolean usableInInventory() {
			return true;
		}

		@Override
		public int getElementAmount(ElementType type) {
			return stack.getOrDefault(ECDataComponents.ELEMENT_AMOUNTS, ElementAmounts.EMPTY).get(type);
		}

		@Override
		public int getElementCapacity(ElementType type) {
			return PureElementHolderItem.this.getElementCapacity();
		}

		@Override
		public int insertElement(int count, ElementType type, boolean simulate) {
			int amount = getElementAmount(type);
			int newCount = Math.min(amount + count, getElementCapacity(type));
			int ret = count - newCount + amount;

			if (!simulate) {
				setAmount(type, newCount);
			}
			return ret;
		}

		@Override
		public int extractElement(int count, ElementType type, boolean simulate) {
			int amount = getElementAmount(type);
			int newCount = Math.max(amount - count, 0);
			int ret = amount - newCount;

			if (!simulate) {
				setAmount(type, newCount);
			}
			return ret;
		}

		@Override
		public void fill() {
			stack.set(ECDataComponents.ELEMENT_AMOUNTS, stack.getOrDefault(ECDataComponents.ELEMENT_AMOUNTS, ElementAmounts.EMPTY)
					.with(ElementType.FIRE, getElementCapacity(ElementType.FIRE))
					.with(ElementType.WATER, getElementCapacity(ElementType.WATER))
					.with(ElementType.EARTH, getElementCapacity(ElementType.EARTH))
					.with(ElementType.AIR, getElementCapacity(ElementType.AIR)));
		}

		@Override
		public void fill(ElementType type) {
			setAmount(type, getElementCapacity(type));
		}

		private void setAmount(ElementType type, int newCount) {
			stack.set(ECDataComponents.ELEMENT_AMOUNTS, stack.getOrDefault(ECDataComponents.ELEMENT_AMOUNTS, ElementAmounts.EMPTY).with(type, newCount));
		}
	}
}
