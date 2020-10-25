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
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.loot.LootHelper;
import sirttas.elementalcraft.spell.Spell;

public class SpellSilkVein extends Spell {

	public static final String NAME = "silk_vein";

	public SpellSilkVein() {
		super(Properties.create(Spell.Type.UTILITY).elementType(ElementType.EARTH).cooldown(ECConfig.CONFIG.silkVeinCooldown.get()).consumeAmount(ECConfig.CONFIG.silkVeinConsumeAmount.get()));
	}

	private boolean isValidBlock(Block block) {
		return Tags.Blocks.ORES.contains(block);
	}

	private void mineVein(World world, BlockPos target) {
		Queue<BlockPos> queue = new ArrayDeque<>();
		double rangeSq = ECConfig.CONFIG.silkVeinRange.get();

		rangeSq *= rangeSq;
		queue.offer(target);
		while (!queue.isEmpty()) {
			BlockPos pos = queue.poll();
			Block block = world.getBlockState(pos).getBlock();
			
			if (isValidBlock(block) && pos.distanceSq(target) <= rangeSq) {
				if (world instanceof ServerWorld) {
					LootHelper.getDrops((ServerWorld) world, pos, true).forEach(stack -> Block.spawnAsEntity(world, pos, stack));
				}
				world.destroyBlock(pos, false);
				Stream.of(Direction.values()).forEach(d -> queue.offer(pos.offset(d)));
			}
		}
	}

	@Override
	public ActionResultType castOnBlock(Entity sender, BlockPos target) {
		World world = sender.getEntityWorld();

		if (isValidBlock(world.getBlockState(target).getBlock())) {
			mineVein(world, target);
			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}


}
