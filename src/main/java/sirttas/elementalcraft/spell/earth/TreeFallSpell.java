package sirttas.elementalcraft.spell.earth;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellCastResult;
import sirttas.elementalcraft.spell.properties.SpellProperties;
import sirttas.elementalcraft.tag.ECTags;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.stream.Stream;

public class TreeFallSpell extends Spell {

	public static final String NAME = "tree_fall";

	public TreeFallSpell(Holder<SpellProperties> properties) {
		super(properties);
	}

	private boolean isValidBlock(BlockState state) {
		return state.is(ECTags.Blocks.TREE_PARTS);
	}

	private void cutTree(Entity sender, Level level, BlockPos target) {
		Queue<BlockPos> queue = new ArrayDeque<>();
		float rangeSq = getRange(sender);

		rangeSq *= rangeSq;
		queue.offer(target);
		while (!queue.isEmpty()) {
			BlockPos pos = queue.poll();
			var state = level.getBlockState(pos);
			
			if (isValidBlock(state) && pos.distSqr(target) <= rangeSq) {
				level.destroyBlock(pos, true);
				Stream.of(Direction.values()).filter(d -> d != Direction.DOWN).forEach(d -> queue.offer(pos.relative(d)));
			}
		}
	}

	@Override
	public SpellCastResult castOnBlock(Level level, Entity sender, BlockPos target, BlockHitResult hitResult) {
		if (level.isClientSide() || !isValidBlock(level.getBlockState(target))) {
            return SpellCastResult.PASS;
        }

        cutTree(sender, level, target);
        return SpellCastResult.SUCCESS;

    }
}
