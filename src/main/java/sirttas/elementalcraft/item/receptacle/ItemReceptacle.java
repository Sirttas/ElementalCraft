package sirttas.elementalcraft.item.receptacle;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.property.ECProperties;

public class ItemReceptacle extends ItemEC {

	public static final String NAME = "receptacle";

	public ItemReceptacle() {
		super(ECProperties.ITEM_UNSTACKABLE);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		ElementType elementType = ReceptacleHelper.getElementType(context.getItem());
		BlockItemUseContext newContext = new BlockItemUseContext(context);

		if (newContext.canPlace()) {
			world.setBlockState(newContext.getPos(), ECBlocks.source.getDefaultState().with(ECProperties.ELEMENT_TYPE, elementType));
			context.getPlayer().setItemStackToSlot(context.getHand() == Hand.MAIN_HAND ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND,
					Boolean.TRUE.equals(ECConfig.CONFIG.conserveReceptacle.get()) ? ReceptacleHelper.createStack(ElementType.NONE) : ItemStack.EMPTY);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;

	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		return this.getTranslationKey() + '.' + ReceptacleHelper.getElementType(stack).func_176610_l/* getName */();
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
