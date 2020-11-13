package sirttas.elementalcraft.item.receptacle;

import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.property.ECProperties;

public class ItemReceptacle extends AbstractReceptacle {

	public static final String NAME = "receptacle";

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		ItemStack sourceReceptacle = context.getItem();
		ElementType elementType = ReceptacleHelper.getElementType(sourceReceptacle);
		BlockItemUseContext newContext = new BlockItemUseContext(context);

		if (newContext.canPlace()) {
			world.setBlockState(newContext.getPos(), ECBlocks.source.getDefaultState().with(ECProperties.ELEMENT_TYPE, elementType));
			if (!context.getPlayer().isCreative()) {
				ItemStack stack = ReceptacleHelper.createFrom(sourceReceptacle, ElementType.NONE);

				if (!ReceptacleHelper.areReceptaclesUnbreakable()) {
					stack.damageItem(1, context.getPlayer(), p -> p.sendBreakAnimation(context.getHand()));
				}
				context.getPlayer().setHeldItem(context.getHand(), stack);
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
					items.add(ReceptacleHelper.createStack(elementType));
				}
			}
		}
	}

}
