package sirttas.elementalcraft.spell.earth;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.spell.Spell;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.stream.Stream;

public class TreeFallSpell extends Spell {

	public static final String NAME = "tree_fall";

	public TreeFallSpell(ResourceKey<Spell> key) {
		super(key);
	}

	private boolean isValidBlock(BlockState state) {
		return state.is(BlockTags.LOGS) ||state.is(BlockTags.LEAVES);
	}

	private void cutTree(Entity sender, Level world, BlockPos target) {
		Queue<BlockPos> queue = new ArrayDeque<>();
		float rangeSq = getRange(sender);

		rangeSq *= rangeSq;
		queue.offer(target);
		while (!queue.isEmpty()) {
			BlockPos pos = queue.poll();
			var state = world.getBlockState(pos);
			
			if (isValidBlock(state) && pos.distSqr(target) <= rangeSq) {
				world.destroyBlock(pos, true);
				Stream.of(Direction.values()).filter(d -> d != Direction.DOWN).forEach(d -> queue.offer(pos.relative(d)));
			}
		}
	}

	@Override
	public InteractionResult castOnBlock(Entity sender, BlockPos target) {
		Level world = sender.getCommandSenderWorld();

		if (!world.isClientSide && isValidBlock(world.getBlockState(target))) {
			cutTree(sender, world, target);
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}


}
