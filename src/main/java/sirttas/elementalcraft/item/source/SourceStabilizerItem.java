package sirttas.elementalcraft.item.source;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.source.SourceBlockEntity;
import sirttas.elementalcraft.entity.player.ECPlayerHelper;

import javax.annotation.Nonnull;

public class SourceStabilizerItem extends Item {

	public static final String NAME = "source_stabilizer";
	
	public SourceStabilizerItem(Item.Properties properties) {
		super(properties);
	}
	
	@Nonnull
    @Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		ItemStack stack = context.getItemInHand();
		Player player = context.getPlayer();
		
		return BlockEntityHelper.getBlockEntityAs(level, pos, SourceBlockEntity.class)
				.map(source -> {
					if (player != null && !source.isStabilized()) {
						source.setStabilized(true);
						ECPlayerHelper.shrinkItemInHand(player, stack, context.getHand());
						return InteractionResult.SUCCESS;
					}
					return InteractionResult.PASS;
				}).orElse(InteractionResult.PASS);
	}
}
