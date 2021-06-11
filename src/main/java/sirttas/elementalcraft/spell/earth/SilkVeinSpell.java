package sirttas.elementalcraft.spell.earth;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;
import sirttas.elementalcraft.loot.LootHelper;
import sirttas.elementalcraft.spell.Spell;

public class SilkVeinSpell extends Spell {

	public static final String NAME = "silk_vein";

	private boolean isValidBlock(Block block) {
		return Tags.Blocks.ORES.contains(block);
	}

	private void mineVein(Entity sender, World world, BlockPos target) {
		Queue<BlockPos> queue = new ArrayDeque<>();
		float rangeSq = getRange(sender);

		rangeSq *= rangeSq;
		queue.offer(target);
		while (!queue.isEmpty()) {
			BlockPos pos = queue.poll();
			Block block = world.getBlockState(pos).getBlock();
			
			if (isValidBlock(block) && pos.distSqr(target) <= rangeSq) {
				if (world instanceof ServerWorld) {
					LootHelper.getDrops((ServerWorld) world, pos, true).forEach(stack -> Block.popResource(world, pos, stack));
				}
				world.destroyBlock(pos, false);
				Stream.of(Direction.values()).forEach(d -> queue.offer(pos.relative(d)));
			}
		}
	}

	@Override
	public ActionResultType castOnBlock(Entity sender, BlockPos target) {
		World world = sender.getCommandSenderWorld();

		if (!world.isClientSide && isValidBlock(world.getBlockState(target).getBlock())) {
			mineVein(sender, world, target);
			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}


}
