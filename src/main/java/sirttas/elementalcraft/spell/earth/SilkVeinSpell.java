package sirttas.elementalcraft.spell.earth;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.stream.Stream;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.Tags;
import sirttas.elementalcraft.loot.LootHelper;
import sirttas.elementalcraft.spell.Spell;

public class SilkVeinSpell extends Spell {

	public static final String NAME = "silk_vein";

	private boolean isValidBlock(Block block) {
		return Tags.Blocks.ORES.contains(block);
	}

	private void mineVein(Entity sender, Level world, BlockPos target) {
		Queue<BlockPos> queue = new ArrayDeque<>();
		float rangeSq = getRange(sender);

		rangeSq *= rangeSq;
		queue.offer(target);
		while (!queue.isEmpty()) {
			BlockPos pos = queue.poll();
			Block block = world.getBlockState(pos).getBlock();
			
			if (isValidBlock(block) && pos.distSqr(target) <= rangeSq) {
				if (world instanceof ServerLevel) {
					LootHelper.getDrops((ServerLevel) world, pos, true).forEach(stack -> Block.popResource(world, pos, stack));
				}
				world.destroyBlock(pos, false);
				Stream.of(Direction.values()).forEach(d -> queue.offer(pos.relative(d)));
			}
		}
	}

	@Override
	public InteractionResult castOnBlock(Entity sender, BlockPos target) {
		Level world = sender.getCommandSenderWorld();

		if (!world.isClientSide && isValidBlock(world.getBlockState(target).getBlock())) {
			mineVein(sender, world, target);
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}


}
