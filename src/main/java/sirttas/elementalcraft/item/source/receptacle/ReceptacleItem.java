package sirttas.elementalcraft.item.source.receptacle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.holder.CapabilitySourceTraitHolder;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.source.SourceBlockEntity;
import sirttas.elementalcraft.block.source.trait.holder.SourceTraitHolder;
import sirttas.elementalcraft.item.ECItem;
import sirttas.elementalcraft.property.ECProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ReceptacleItem extends ECItem {

	public static final String NAME = "receptacle";

	public ReceptacleItem() {
		super(ECProperties.Items.ITEM_UNSTACKABLE);
	}

	@Override
	@Nullable
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		var holder = new SourceTraitHolder();

		if (holder.isEmpty()) {
			var tag = stack.getTag();

			if (tag != null && tag.contains(ECNames.BLOCK_ENTITY_TAG)) {
				var blockEntityTag = tag.getCompound(ECNames.BLOCK_ENTITY_TAG);

				if (blockEntityTag.contains(ECNames.TRAITS_HOLDER)) {
					holder.deserializeNBT(blockEntityTag.getCompound(ECNames.TRAITS_HOLDER));
				}
			}
		}
		return CapabilitySourceTraitHolder.createProvider(holder);
	}

	@Nonnull
    @Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		ItemStack sourceReceptacle = context.getItemInHand();
		ElementType elementType = ReceptacleHelper.getElementType(sourceReceptacle);
		Player player = context.getPlayer();
		InteractionHand hand = context.getHand();
		BlockPlaceContext newContext = new BlockPlaceContext(context);
		BlockPos pos = newContext.getClickedPos();

		if (newContext.canPlace()) {
			world.setBlockAndUpdate(pos, ECBlocks.SOURCE.get().defaultBlockState().setValue(ElementType.STATE_PROPERTY, elementType));
			BlockItem.updateCustomBlockEntityTag(world, player, pos, sourceReceptacle);
			if (player != null && !player.getAbilities().instabuild) {
				BlockEntityHelper.getBlockEntityAs(world, pos, SourceBlockEntity.class).ifPresent(SourceBlockEntity::exhaust);
				player.setItemInHand(hand, ItemStack.EMPTY);
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Nonnull
    @Override
	public String getDescriptionId(@Nonnull ItemStack stack) {
		return this.getDescriptionId() + '.' + ReceptacleHelper.getElementType(stack).getSerializedName();
	}

	@Override
	public void fillItemCategory(@Nonnull CreativeModeTab group, @Nonnull NonNullList<ItemStack> items) {
		if (this.allowedIn(group)) {
			for (ElementType elementType : ElementType.values()) {
				if (elementType != ElementType.NONE) {
					items.add(ReceptacleHelper.create(elementType));
				}
			}
		}
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
		var blockEntityTag = stack.getTagElement(ECNames.BLOCK_ENTITY_TAG);
		
		if (blockEntityTag != null) {
			boolean analyzed = blockEntityTag.getBoolean(ECNames.ANALYZED);
			
			if (analyzed) {
				blockEntityTag.putBoolean(ECNames.ANALYZED, true);
				for (var value : getSourceTraitValues(stack)) {
					tooltip.add(value.getDescription());
				}
			}
		}
	}

	@Nonnull
	private Collection<ISourceTraitValue> getSourceTraitValues(ItemStack stack) {
		return CapabilitySourceTraitHolder.get(stack).map(h -> h.getTraits().values()).orElse(Collections.emptyList());
	}
}
