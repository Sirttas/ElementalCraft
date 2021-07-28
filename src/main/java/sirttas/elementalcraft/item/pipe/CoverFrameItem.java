package sirttas.elementalcraft.item.pipe;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock.CoverType;
import sirttas.elementalcraft.item.ECItem;

public class CoverFrameItem extends ECItem {

	public static final String NAME = "cover_frame";

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		ItemStack stack = context.getItemInHand();
		Player player = context.getPlayer();
		BlockState state = world.getBlockState(pos);

		if (state.getBlock() instanceof ElementPipeBlock && state.getValue(ElementPipeBlock.COVER) == CoverType.NONE) {
			world.setBlockAndUpdate(pos, state.setValue(ElementPipeBlock.COVER, CoverType.FRAME));
			if (!player.isCreative()) {
				stack.shrink(1);
				if (stack.isEmpty()) {
					player.setItemInHand(context.getHand(), ItemStack.EMPTY);
				}
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
	
}
