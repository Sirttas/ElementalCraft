package sirttas.elementalcraft.spell.earth;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.stream.Stream;

public class TreeFallSpell extends Spell {

	public static final String NAME = "tree_fall";

	public TreeFallSpell(ResourceKey<Spell> key) {
		super(key);
	}

	private boolean isValidBlock(BlockState state) {
		return state.is(ECTags.Blocks.TREE_PARTS);
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

	@Nonnull
	@Override
	public InteractionResult castOnBlock(@Nonnull Entity sender, @Nonnull BlockPos target, @Nonnull BlockHitResult hitResult) {
		Level world = sender.level();

		if (!world.isClientSide && isValidBlock(world.getBlockState(target))) {
			cutTree(sender, world, target);
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}
}
