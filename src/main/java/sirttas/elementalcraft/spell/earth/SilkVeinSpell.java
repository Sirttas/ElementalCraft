package sirttas.elementalcraft.spell.earth;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.Tags;
import sirttas.elementalcraft.loot.LootHelper;
import sirttas.elementalcraft.spell.Spell;

import javax.annotation.Nonnull;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.stream.Stream;

public class SilkVeinSpell extends Spell {

	public static final String NAME = "silk_vein";

	public SilkVeinSpell(ResourceKey<Spell> key) {
		super(key);
	}

	private boolean isValidBlock(BlockState state) {
		return state.is(Tags.Blocks.ORES);
	}

	private void mineVein(Entity sender, Level world, BlockPos target) {
		Queue<BlockPos> queue = new ArrayDeque<>();
		float rangeSq = getRange(sender);

		rangeSq *= rangeSq;
		queue.offer(target);
		while (!queue.isEmpty()) {
			BlockPos pos = queue.poll();
			var state = world.getBlockState(pos);
			
			if (isValidBlock(state) && pos.distSqr(target) <= rangeSq) {
				if (world instanceof ServerLevel) {
					LootHelper.getDrops((ServerLevel) world, pos, true).forEach(stack -> Block.popResource(world, pos, stack));
				}
				world.destroyBlock(pos, false);
				Stream.of(Direction.values()).forEach(d -> queue.offer(pos.relative(d)));
			}
		}
	}

	@Nonnull
	@Override
	public InteractionResult castOnBlock(@Nonnull Entity sender, @Nonnull BlockPos target, @Nonnull BlockHitResult hitResult) {
		Level world = sender.getLevel();

		if (!world.isClientSide && isValidBlock(world.getBlockState(target))) {
			mineVein(sender, world, target);
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}


}
