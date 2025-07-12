package sirttas.elementalcraft.item.holder;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.element.storage.AbstractItemStackSingleElementStorage;

import javax.annotation.Nonnull;

public class ElementHolderItem extends AbstractElementHolderItem implements IElementTypeProvider {

	public static final String NAME = "element_holder";
	public static final String NAME_FIRE = NAME + "_fire";
	public static final String NAME_WATER = NAME + "_water";
	public static final String NAME_EARTH = NAME + "_earth";
	public static final String NAME_AIR = NAME + "_air";

	private final ElementType elementType;

	public ElementHolderItem(ElementType elementType, Item.Properties properties) {
		super(ECConfig.SERVER.elementHolderCapacity::get, ECConfig.SERVER.elementHolderTransferAmount::get, properties);
		this.elementType = elementType;
	}

	@Override
	public @NotNull ElementType getElementType() {
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
	
	private class ElementStorage extends AbstractItemStackSingleElementStorage {

		private ElementStorage(ItemStack stack) {
			super(stack);

		}

		@Override
		public boolean usableInInventory() {
			return true;
		}

		@Override
		public int getElementCapacity() {
			return ElementHolderItem.this.getElementCapacity();
		}


		@NotNull
		@Override
		public ElementType getElementType() {
			return ElementHolderItem.this.elementType;
		}
	}
}
