package sirttas.elementalcraft.item.holder;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.ISourceInteractable;
import sirttas.elementalcraft.config.ECConfig;

public class PureElementHolderItem extends AbstractElementHolderItem implements ISourceInteractable {

	public static final String NAME = "pure_element_holder";

	public PureElementHolderItem() {
		super(ECConfig.COMMON.pureElementHolderCapacity.get(), ECConfig.COMMON.pureElementHolderTransferAmount.get());
	}
	
	@Override
	@Nullable
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
		ElementStorage storage = new ElementStorage(stack);
		
		if (nbt != null && nbt.contains(ECNames.PARENT)) {
			storage.deserializeNBT(nbt.getCompound(ECNames.PARENT));
		}
		return CapabilityElementStorage.createProvider(storage);
	}

	@Override
	public IElementStorage getElementStorage(ItemStack stack) {
		return CapabilityElementStorage.get(stack).orElse(new ElementStorage(stack));
	}
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		ElementType.ALL_VALID.forEach(elementType -> tooltip
				.add(new TranslationTextComponent("tooltip.elementalcraft.element_type_percent_full",
						elementType.getDisplayName(),
						ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(getElementStorage(stack).getElementAmount(elementType) * 100 / elementCapacity))
						.withStyle(TextFormatting.GREEN)));
	}

	@Override
	public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.allowdedIn(group)) {
			ItemStack full = new ItemStack(this);
			IElementStorage storage = getElementStorage(full);

			ElementType.ALL_VALID.forEach(elementType -> storage.insertElement(elementCapacity, elementType, false));
			items.add(new ItemStack(this));
			items.add(full);
		}
	}
	
	@Override
	protected ElementType getElementType(IElementStorage target, BlockState blockstate) {
		if (blockstate.hasProperty(ElementType.STATE_PROPERTY)) {
			return ElementType.getElementType(blockstate);
		}
		if (target instanceof IElementTypeProvider) {
			return ((IElementTypeProvider) target).getElementType();
		}
		return ElementType.NONE;
	}
	
	private class ElementStorage implements IElementStorage, INBTSerializable<CompoundNBT> {

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
			return elementCapacity;
		}

		@Override
		public int insertElement(int count, ElementType type, boolean simulate) {
			int amount = getElementAmount(type);
			int newCount = Math.min(amount + count, elementCapacity);
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
		public CompoundNBT serializeNBT() {
			CompoundNBT compound = new CompoundNBT();

			serializeNBT(compound);
			return compound;
		}

		private void serializeNBT(CompoundNBT compound) {
			amounts.forEach((elementType, amount) -> compound.putInt(elementType.getSerializedName(), amount));
		}
		
		@Override
		public void deserializeNBT(CompoundNBT compound) {
			amounts.replaceAll((elementType, amount) -> {
				if (compound != null && compound.contains(elementType.getSerializedName())) {
					return compound.getInt(elementType.getSerializedName());
				}
				return 0;
			});
		}
	}
}
