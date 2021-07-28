package sirttas.elementalcraft.spell.earth;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.stream.Stream;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.spell.Spell;

public class TreeFallSpell extends Spell {

	public static final String NAME = "tree_fall";

	private boolean isValidBlock(Block block) {
		return BlockTags.LOGS.contains(block) || BlockTags.LEAVES.contains(block);
	}

	private void cutTree(Entity sender, Level world, BlockPos target) {
		Queue<BlockPos> queue = new ArrayDeque<>();
		float rangeSq = getRange(sender);

		rangeSq *= rangeSq;
		queue.offer(target);
		while (!queue.isEmpty()) {
			BlockPos pos = queue.poll();
			Block block = world.getBlockState(pos).getBlock();
			
			if (isValidBlock(block) && pos.distSqr(target) <= rangeSq) {
				world.destroyBlock(pos, true);
				Stream.of(Direction.values()).filter(d -> d != Direction.DOWN).forEach(d -> queue.offer(pos.relative(d)));
			}
		}
	}

	@Override
	public InteractionResult castOnBlock(Entity sender, BlockPos target) {
		Level world = sender.getCommandSenderWorld();

		if (!world.isClientSide && isValidBlock(world.getBlockState(target).getBlock())) {
			cutTree(sender, world, target);
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}


}
