package sirttas.elementalcraft.item.source.receptacle;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.source.trait.holder.ISourceTraitHolder;
import sirttas.elementalcraft.block.source.SourceBlock;
import sirttas.elementalcraft.block.source.trait.holder.ItemSourceTraitHolder;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.element.storage.AbstractItemStackSingleElementStorage;

import java.util.function.Consumer;

public class ReceptacleItem extends BlockItem implements IElementTypeProvider {

	public ReceptacleItem(SourceBlock source, Item.Properties properties) {
		super(source, properties);
	}

	@NotNull
	@Override
	public ElementType getElementType() {
		return ((SourceBlock) getBlock()).getElementType();
	}

    @NotNull
	public static ISourceTraitHolder getTraitHolder(ItemStack stack) {
		return stack.getOrDefault(ECDataComponents.SOURCE_TRAITS_HOLDER, ItemSourceTraitHolder.EMPTY);
	}

	public ISingleElementStorage getElementStorage(ItemStack stack) {
		return new ElementStorage(stack);
	}

    @Override
    @Deprecated
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext context, @NotNull TooltipDisplay display, @NotNull Consumer<Component> builder, @NotNull TooltipFlag tooltipFlag) {
		boolean analyzed = Boolean.TRUE.equals(itemStack.get(ECDataComponents.SOURCE_ANALYZED));

		if (analyzed) {
			for (var value : getTraitHolder(itemStack).getTraits().values()) {
                builder.accept(value.getDescription());
			}
		} else {
            builder.accept(Component.translatable("tooltip.elementalcraft.source.unanalyzed"));
		}
	}

	private class ElementStorage extends AbstractItemStackSingleElementStorage {

		private ElementStorage(ItemStack stack) {
			super(stack);
		}

		@Override
		public int getElementCapacity() {
			return getTraitHolder(stack).getCapacity();
		}


		@NotNull
		@Override
		public ElementType getElementType() {
			return ReceptacleItem.this.getElementType();
		}
	}
}
