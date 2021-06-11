package sirttas.elementalcraft.spell.water;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import sirttas.elementalcraft.spell.Spell;

public class RipeningSpell extends Spell {

	public static final String NAME = "ripening";

	@Override
	public ActionResultType castOnBlock(Entity sender, BlockPos target) {
		World world = sender.getCommandSenderWorld();
		BlockState state = world.getBlockState(target);
		Block block = state.getBlock();

		if (block instanceof IGrowable) {
			IGrowable growable = (IGrowable) block;

			if (growable.isBonemealSuccess(world, world.random, target, state)) {
				if (world instanceof ServerWorld) {
					for (int i = 0; i < 10 && growable.isValidBonemealTarget(world, target, state, world.isClientSide); i++) {
						growable.performBonemeal((ServerWorld) world, world.random, target, state);
						state = world.getBlockState(target);
					}
					world.levelEvent(2005, target, 0);
				}
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}
}
