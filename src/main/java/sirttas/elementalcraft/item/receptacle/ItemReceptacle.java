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

public class ItemReceptacle extends AbstractReceptacle {

	public static final String NAME = "receptacle";

	public ItemReceptacle() {
	}

	public ItemReceptacle(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		ItemStack sourceReceptacle = context.getItem();
		ElementType elementType = ReceptacleHelper.getElementType(sourceReceptacle);
		PlayerEntity player = context.getPlayer();
		Hand hand = context.getHand();
		BlockItemUseContext newContext = new BlockItemUseContext(context);
		BlockPos pos = newContext.getPos();

		if (newContext.canPlace()) {
			world.setBlockState(pos, ECBlocks.SOURCE.getDefaultState().with(ElementType.STATE_PROPERTY, elementType));
			BlockItem.setTileEntityNBT(world, player, pos, sourceReceptacle);
			if (!player.isCreative()) {
				ItemStack stack = ReceptacleHelper.createFrom(sourceReceptacle, ElementType.NONE);

				if (stack.isDamageable()) {
					stack.damageItem(1, player, p -> p.sendBreakAnimation(hand));
				}
				player.setHeldItem(hand, stack);
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		return this.getTranslationKey() + '.' + ReceptacleHelper.getElementType(stack).getString();
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.isInGroup(group)) {
			for (ElementType elementType : ElementType.values()) {
				if (elementType != ElementType.NONE) {
					items.add(ReceptacleHelper.create(elementType));
				}
			}
		}
	}

}
