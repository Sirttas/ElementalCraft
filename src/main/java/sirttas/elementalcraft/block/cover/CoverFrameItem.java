package sirttas.elementalcraft.block.cover;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
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
        var level = context.getLevel();
        var pos = context.getClickedPos();
        var stack = context.getItemInHand();
        var player = context.getPlayer();
        var coverable = level.getCapability(Coverable.CAPABILITY, pos);

		if (coverable != null && !coverable.hasFrame()) {
            coverable.putFrame();
			if (player != null) {
				ECPlayerHelper.shrinkItemInHand(player, stack, context.getHand());
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
	
}
