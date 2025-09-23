package sirttas.elementalcraft.item.source.receptacle;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.source.SourceBlockEntity;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.entity.player.ECPlayerHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EmptyReceptacleItem extends Item {

	public static final String NAME = "empty_receptacle";

	public EmptyReceptacleItem(Properties properties) {
		super(properties);
	}

	@Nonnull
    @Override
	public InteractionResult useOn(UseOnContext context) {
		var level = context.getLevel();
		var pos = context.getClickedPos();
		var blockstate = level.getBlockState(pos);
		var player = context.getPlayer();
		var hand = context.getHand();

		if (blockstate.is(ECTags.Blocks.SOURCES)) {
			if (!level.isClientSide) {
				spawnReceptacle(level, pos, player);
				level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
				if (player != null) {
					ECPlayerHelper.shrinkItemInHand(player, context.getItemInHand(), hand);
				}
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	private static void spawnReceptacle(Level level, BlockPos pos, @Nullable Player player) {
		var source = BlockEntityHelper.getBlockEntityAs(level, pos, SourceBlockEntity.class).orElse(null);

		if (source == null) {
			return;
		}

		var receptacle = ReceptacleHelper.create(source.getElementType());

		receptacle.applyComponents(source.collectComponents());
		dropItem(level, pos, player, receptacle);

		if (source.isStabilized()) {
			source.setStabilized(false);
			dropItem(level, pos, player, new ItemStack(ECItems.SOURCE_STABILIZER));
		}
	}

	private static void dropItem(Level level, BlockPos pos, @Nullable Player player, ItemStack stack) {
		if (player != null) {
			EntityHelper.dropAtFeet(level, player, stack);
		}else {
			level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, stack));
		}
	}

}
