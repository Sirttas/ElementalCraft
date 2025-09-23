package sirttas.elementalcraft.item.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock.CoverType;
import sirttas.elementalcraft.entity.player.ECPlayerHelper;

import javax.annotation.Nonnull;

public class CoverFrameItem extends Item {

	public static final String NAME = "cover_frame";

	public CoverFrameItem(Properties properties) {
		super(properties);
	}

	@Nonnull
    @Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		ItemStack stack = context.getItemInHand();
		Player player = context.getPlayer();
		BlockState state = level.getBlockState(pos);

		if (state.getBlock() instanceof ElementPipeBlock && state.getValue(ElementPipeBlock.COVER) == CoverType.NONE) {
			level.setBlockAndUpdate(pos, state.setValue(ElementPipeBlock.COVER, CoverType.FRAME));
			if (player != null) {
				ECPlayerHelper.shrinkItemInHand(player, stack, context.getHand());
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
	
}
