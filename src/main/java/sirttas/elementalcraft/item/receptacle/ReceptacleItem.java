package sirttas.elementalcraft.item.receptacle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;

public class ReceptacleItem extends AbstractReceptacleItem {

	public static final String NAME = "receptacle";

	public ReceptacleItem() {
	}

	public ReceptacleItem(Properties properties) {
		super(properties);
	}

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
			world.setBlockAndUpdate(pos, ECBlocks.SOURCE.defaultBlockState().setValue(ElementType.STATE_PROPERTY, elementType));
			BlockItem.updateCustomBlockEntityTag(world, player, pos, sourceReceptacle);
			if (!player.isCreative()) {
				ItemStack stack = ReceptacleHelper.createFrom(sourceReceptacle, ElementType.NONE);

				if (stack.isDamageableItem()) {
					stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
				}
				player.setItemInHand(hand, stack);
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	public String getDescriptionId(ItemStack stack) {
		return this.getDescriptionId() + '.' + ReceptacleHelper.getElementType(stack).getSerializedName();
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (this.allowdedIn(group)) {
			for (ElementType elementType : ElementType.values()) {
				if (elementType != ElementType.NONE) {
					items.add(ReceptacleHelper.create(elementType));
				}
			}
		}
	}

}
