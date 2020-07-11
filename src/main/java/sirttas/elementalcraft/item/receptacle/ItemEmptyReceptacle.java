package sirttas.elementalcraft.item.receptacle;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.property.ECProperties;

public class ItemEmptyReceptacle extends ItemEC implements ISourceInteractable {

	public static final String NAME = "receptacle_empty";

	public ItemEmptyReceptacle() {
		super(ECProperties.ITEM_UNSTACKABLE);
	}

	@Override
	public boolean canIteractWithSource(ItemStack stack) {
		return true;
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getPos();
		BlockState blockstate = world.getBlockState(pos);

		if (blockstate.getBlock() == ECBlocks.source) {
			if (!world.isRemote) {
				context.getPlayer().setItemStackToSlot(context.getHand() == Hand.MAIN_HAND ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND,
						ReceptacleHelper.createStack(blockstate.get(ECProperties.ELEMENT_TYPE)));
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

}
