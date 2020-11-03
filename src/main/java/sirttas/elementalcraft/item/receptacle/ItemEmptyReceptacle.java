package sirttas.elementalcraft.item.receptacle;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sirttas.elementalcraft.api.source.ISourceInteractable;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.property.ECProperties;

public class ItemEmptyReceptacle extends AbstractReceptacle implements ISourceInteractable {

	public static final String NAME = "receptacle_empty";

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getPos();
		BlockState blockstate = world.getBlockState(pos);

		if (blockstate.getBlock() == ECBlocks.source) {
			if (!world.isRemote) {
				context.getPlayer().setHeldItem(context.getHand(), ReceptacleHelper.createFrom(context.getItem(), blockstate.get(ECProperties.ELEMENT_TYPE)));
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

}
