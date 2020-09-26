package sirttas.elementalcraft.item.receptacle;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.property.ECProperties;

public class ItemEmptyReceptacle extends ItemEC implements ISourceInteractable {

	public static final String NAME = "receptacle_empty";

	public ItemEmptyReceptacle() {
		super(ECProperties.Items.RECEPTACLE);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getPos();
		BlockState blockstate = world.getBlockState(pos);

		if (blockstate.getBlock() == ECBlocks.source) {
			if (!world.isRemote) {
				ItemStack stack = ReceptacleHelper.createStack(blockstate.get(ECProperties.ELEMENT_TYPE));

				stack.setDamage(context.getItem().getDamage());
				context.getPlayer().setHeldItem(context.getHand(), stack);
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return repair.getItem() == ECItems.swiftAlloyIngot;
	}

}
