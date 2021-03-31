package sirttas.elementalcraft.item.cover;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sirttas.elementalcraft.block.pipe.BlockElementPipe;
import sirttas.elementalcraft.block.pipe.BlockElementPipe.CoverType;
import sirttas.elementalcraft.item.ItemEC;

public class ItemCoverFrame extends ItemEC {

	public static final String NAME = "cover_frame";

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getPos();
		ItemStack stack = context.getItem();
		PlayerEntity player = context.getPlayer();
		BlockState state = world.getBlockState(pos);

		if (state.getBlock() instanceof BlockElementPipe && state.get(BlockElementPipe.COVER) == CoverType.NONE) {
			world.setBlockState(pos, state.with(BlockElementPipe.COVER, CoverType.FRAME));
			if (!player.isCreative()) {
				stack.shrink(1);
				if (stack.isEmpty()) {
					player.setHeldItem(context.getHand(), ItemStack.EMPTY);
				}
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
	
}
