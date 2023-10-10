package sirttas.elementalcraft.item.holder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.ElementStorageHelper;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.ISourceInteractable;
import sirttas.elementalcraft.config.ECConfig;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;

public class PureElementHolderItem extends AbstractElementHolderItem implements ISourceInteractable {

	public static final String NAME = "pure_element_holder";

	public PureElementHolderItem() {
		super(ECConfig.COMMON.pureElementHolderCapacity::get, ECConfig.COMMON.pureElementHolderTransferAmount::get);
	}
	
	@Override
	@Nullable
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		ElementStorage storage = new ElementStorage(stack);
		
		if (nbt != null && nbt.contains(ECNames.PARENT)) {
			storage.deserializeNBT(nbt.getCompound(ECNames.PARENT));
		}
		return ElementStorageHelper.createProvider(storage);
	}

	@Override
	public IElementStorage getElementStorage(ItemStack stack) {
		return ElementStorageHelper.get(stack).orElse(new ElementStorage(stack));
	}

	@Override
	protected ElementType getElementType(IElementStorage target, BlockState blockstate) {
		if (blockstate.hasProperty(ElementType.STATE_PROPERTY)) {
			return ElementType.getElementType(blockstate);
		}
		if (target instanceof IElementTypeProvider provider) {
			return provider.getElementType();
		}
		return ElementType.NONE;
	}
	
	private class ElementStorage implements IElementStorage, INBTSerializable<CompoundTag> {

		private final ItemStack stack;
		private final Map<ElementType, Integer> amounts = new EnumMap<>(ElementType.class);
		
		public ElementStorage(ItemStack stack) {
			this.stack = stack;
			ElementType.ALL_VALID.forEach(type -> amounts.put(type, 0));
		}

		@Override
		public boolean usableInInventory() {
			return true;
		}

		@Override
		public int getElementAmount(ElementType type) {
			refresh();
			return amounts.getOrDefault(type, 0);
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
				amounts.put(type, newCount);
				updateAmount();
			}
			return ret;
		}

		@Override
		public int extractElement(int count, ElementType type, boolean simulate) {
			int amount = getElementAmount(type);
			int newCount = Math.max(amount - count, 0);
			int ret = amount - newCount;

			if (!simulate) {
				amounts.put(type, newCount);
				updateAmount();
			}
			return ret;
		}
		
		private void refresh() {
			deserializeNBT(stack.getTag());
		}
		
		private void updateAmount() {
			serializeNBT(stack.getOrCreateTag());
		}
		
		@Override
		public CompoundTag serializeNBT() {
			CompoundTag compound = new CompoundTag();

			serializeNBT(compound);
			return compound;
		}

		private void serializeNBT(CompoundTag compound) {
			amounts.forEach((elementType, amount) -> compound.putInt(elementType.getSerializedName(), amount));
		}
		
		@Override
		public void deserializeNBT(CompoundTag compound) {
			amounts.replaceAll((elementType, amount) -> {
				if (compound != null && compound.contains(elementType.getSerializedName())) {
					return compound.getInt(elementType.getSerializedName());
				}
				return 0;
			});
		}

		@Override
		public void fill() {
			amounts.replaceAll((elementType, amount) -> getElementCapacity(elementType));
			updateAmount();
		}

		@Override
		public void fill(ElementType type) {
			amounts.put(type, getElementCapacity(type));
			updateAmount();
		}
	}
}
