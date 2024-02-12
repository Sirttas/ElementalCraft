package sirttas.elementalcraft.item.holder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.element.storage.single.StaticElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.ISourceInteractable;
import sirttas.elementalcraft.config.ECConfig;

import javax.annotation.Nonnull;

public class ElementHolderItem extends AbstractElementHolderItem implements ISourceInteractable, IElementTypeProvider {

	public static final String NAME = "element_holder";
	public static final String NAME_FIRE = NAME + "_fire";
	public static final String NAME_WATER = NAME + "_water";
	public static final String NAME_EARTH = NAME + "_earth";
	public static final String NAME_AIR = NAME + "_air";

	private final ElementType elementType;

	public ElementHolderItem(ElementType elementType) {
		super(ECConfig.COMMON.elementHolderCapacity::get, ECConfig.COMMON.elementHolderTransferAmount::get);
		this.elementType = elementType;
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}
	
	@Override
	protected ElementType getElementType(IElementStorage target, BlockState blockstate) {
		return elementType;
	}

	@Override
	public ISingleElementStorage getElementStorage(ItemStack stack) {
		return new ElementStorage(stack);
	}

	@Override
	protected boolean isValidSource(BlockState state) {
		return super.isValidSource(state) && ElementType.getElementType(state) == elementType;
	}

	@Override
	public int getBarColor(@Nonnull ItemStack stack) {
		return elementType.getColor();
	}

	@Override
	public int getBarWidth(@Nonnull ItemStack stack) {
		return Math.round(getElementStorage(stack).getElementAmount() * 13F / getElementCapacity());
	}

	@Override
	public boolean isBarVisible(@Nonnull ItemStack stack) {
		return true;
	}

	@Override
	public boolean canBeDepleted() {
		return true;
	}
	
	private class ElementStorage extends StaticElementStorage {

		private final ItemStack stack;
		
		public ElementStorage(ItemStack stack) {
			super(ElementHolderItem.this.elementType, ElementHolderItem.this.getElementCapacity());
			this.stack = stack;
			refresh();
		}


		@Override
		public boolean usableInInventory() {
			return true;
		}
		
		@Override
		public int getElementAmount() {
			refresh();
			return super.getElementAmount();
		}
		
		@Override
		public int insertElement(int count, ElementType type, boolean simulate) {
			refresh();
			
			int value = super.insertElement(count, type, simulate);

			updateAmount();
			return value;
		}

		@Override
		public int extractElement(int count, ElementType type, boolean simulate) {
			refresh();
			
			int value = super.extractElement(count, type, simulate);

			updateAmount();
			return value;
		}
		

		private void refresh() {
			CompoundTag tag = stack.getTag();
			
			if (tag != null && tag.contains(ECNames.ELEMENT_AMOUNT)) {
				elementAmount = tag.getInt(ECNames.ELEMENT_AMOUNT);
			}
		}
		
		private void updateAmount() {
			stack.getOrCreateTag().putInt(ECNames.ELEMENT_AMOUNT, elementAmount);
		}
	}
}
