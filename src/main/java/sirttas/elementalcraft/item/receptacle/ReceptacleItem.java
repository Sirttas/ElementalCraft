package sirttas.elementalcraft.item.receptacle;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
	public ActionResultType useOn(ItemUseContext context) {
		World world = context.getLevel();
		ItemStack sourceReceptacle = context.getItemInHand();
		ElementType elementType = ReceptacleHelper.getElementType(sourceReceptacle);
		PlayerEntity player = context.getPlayer();
		Hand hand = context.getHand();
		BlockItemUseContext newContext = new BlockItemUseContext(context);
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
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	@Override
	public String getDescriptionId(ItemStack stack) {
		return this.getDescriptionId() + '.' + ReceptacleHelper.getElementType(stack).getSerializedName();
	}

	@Override
	public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.allowdedIn(group)) {
			for (ElementType elementType : ElementType.values()) {
				if (elementType != ElementType.NONE) {
					items.add(ReceptacleHelper.create(elementType));
				}
			}
		}
	}

}
