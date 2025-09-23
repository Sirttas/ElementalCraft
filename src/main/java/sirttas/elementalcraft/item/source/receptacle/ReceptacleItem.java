package sirttas.elementalcraft.item.source.receptacle;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.source.trait.holder.ISourceTraitHolder;
import sirttas.elementalcraft.block.source.SourceBlock;
import sirttas.elementalcraft.block.source.trait.holder.ItemSourceTraitHolder;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.element.storage.AbstractItemStackSingleElementStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ReceptacleItem extends BlockItem implements IElementTypeProvider {

	public ReceptacleItem(SourceBlock source, Item.Properties properties) {
		super(source, properties);
	}

	@NotNull
	@Override
	public ElementType getElementType() {
		return ((SourceBlock) getBlock()).getElementType();
	}

	@Override
	public @NotNull String getDescriptionId() {
		return this.getOrCreateDescriptionId();
	}

	@NotNull
	public static ISourceTraitHolder getTraitHolder(ItemStack stack) {
		return stack.getOrDefault(ECDataComponents.SOURCE_TRAITS_HOLDER, ItemSourceTraitHolder.EMPTY);
	}

	public ISingleElementStorage getElementStorage(ItemStack stack) {
		return new ElementStorage(stack);
	}

	@Override
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable Item.TooltipContext tooltipContext, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
		boolean analyzed = Boolean.TRUE.equals(stack.get(ECDataComponents.SOURCE_ANALYZED));

		if (analyzed) {
			for (var value : getTraitHolder(stack).getTraits().values()) {
				tooltip.add(value.getDescription());
			}
		} else {
			tooltip.add(Component.translatable("tooltip.elementalcraft.source.unanalyzed"));
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
