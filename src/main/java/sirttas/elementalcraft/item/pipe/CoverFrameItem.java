package sirttas.elementalcraft.item.pipe;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock.CoverType;
import sirttas.elementalcraft.item.ECItem;

public class CoverFrameItem extends ECItem {

	public static final String NAME = "cover_frame";

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		World world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		ItemStack stack = context.getItemInHand();
		PlayerEntity player = context.getPlayer();
		BlockState state = world.getBlockState(pos);

		if (state.getBlock() instanceof ElementPipeBlock && state.getValue(ElementPipeBlock.COVER) == CoverType.NONE) {
			world.setBlockAndUpdate(pos, state.setValue(ElementPipeBlock.COVER, CoverType.FRAME));
			if (!player.isCreative()) {
				stack.shrink(1);
				if (stack.isEmpty()) {
					player.setItemInHand(context.getHand(), ItemStack.EMPTY);
				}
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
	
}
