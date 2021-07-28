package sirttas.elementalcraft.item.holder;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.element.storage.single.StaticElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.ISourceInteractable;
import sirttas.elementalcraft.config.ECConfig;

public class ElementHolderItem extends AbstractElementHolderItem implements ISourceInteractable, IElementTypeProvider {

	public static final String NAME = "element_holder";
	public static final String NAME_FIRE = NAME + "_fire";
	public static final String NAME_WATER = NAME + "_water";
	public static final String NAME_EARTH = NAME + "_earth";
	public static final String NAME_AIR = NAME + "_air";

	private final ElementType elementType;

	public ElementHolderItem(ElementType elementType) {
		super(ECConfig.COMMON.elementHolderCapacity.get(), ECConfig.COMMON.elementHolderTransferAmount.get());
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
	@Nullable
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		ElementStorage storage = new ElementStorage(stack);
		
		if (nbt != null && nbt.contains(ECNames.PARENT)) {
			storage.deserializeNBT(nbt.getCompound(ECNames.PARENT));
		}
		return CapabilityElementStorage.createProvider(storage);
	}

	@Override
	public ISingleElementStorage getElementStorage(ItemStack stack) {
		return (ISingleElementStorage) CapabilityElementStorage.get(stack).orElse(new StaticElementStorage(elementType, 0));
	}

	@Override
	protected boolean isValidSource(BlockState state) {
		return super.isValidSource(state) && ElementType.getElementType(state) == elementType;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		tooltip.add(new TranslatableComponent("tooltip.elementalcraft.percent_full",
				ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(getElementStorage(stack).getElementAmount() * 100 / elementCapacity))
				.withStyle(ChatFormatting.GREEN));
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (this.allowdedIn(group)) {
			ItemStack full = new ItemStack(this);
			ISingleElementStorage storage = getElementStorage(full);

			storage.insertElement(elementCapacity, false);
			items.add(new ItemStack(this));
			items.add(full);
		}
	}

	@Override
	public int getDamage(ItemStack stack) {
		return 1000 * (elementCapacity - getElementStorage(stack).getElementAmount()) / elementCapacity;
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return 1000;
	}

	@Override
	public boolean canBeDepleted() {
		return true;
	}
	
	private class ElementStorage extends StaticElementStorage {

		private final ItemStack stack;
		
		public ElementStorage(ItemStack stack) {
			super(ElementHolderItem.this.elementType, ElementHolderItem.this.elementCapacity);
			this.stack = stack;
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
