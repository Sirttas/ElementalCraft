package sirttas.elementalcraft.item.receptacle;

import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.property.ECProperties;

public class ItemReceptacle extends ItemEC {

	public static final String NAME = "receptacle";

	public ItemReceptacle() {
		super(ECProperties.Items.RECEPTACLE);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		ElementType elementType = ReceptacleHelper.getElementType(context.getItem());
		BlockItemUseContext newContext = new BlockItemUseContext(context);

		if (newContext.canPlace()) {
			world.setBlockState(newContext.getPos(), ECBlocks.source.getDefaultState().with(ECProperties.ELEMENT_TYPE, elementType));
			if (!context.getPlayer().isCreative()) {
				ItemStack stack = ReceptacleHelper.createStack(ElementType.NONE);

				if (!ReceptacleHelper.areReceptaclesUnbreakable()) {
					stack.setDamage(context.getItem().getDamage());
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

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return repair.getItem() == Items.GOLD_INGOT;
	}

}
