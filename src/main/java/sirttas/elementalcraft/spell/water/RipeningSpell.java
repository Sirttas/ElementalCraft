package sirttas.elementalcraft.spell.water;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.spell.Spell;

public class RipeningSpell extends Spell {

	public static final String NAME = "ripening";

	public RipeningSpell(ResourceKey<Spell> key) {
		super(key);
	}

	@Override
	public InteractionResult castOnBlock(Entity sender, BlockPos target) {
		Level world = sender.getCommandSenderWorld();
		BlockState state = world.getBlockState(target);
		Block block = state.getBlock();

		if (block instanceof BonemealableBlock growable && growable.isBonemealSuccess(world, world.random, target, state)) {
			if (world instanceof ServerLevel) {
				for (int i = 0; i < 10 && growable.isValidBonemealTarget(world, target, state, world.isClientSide); i++) {
					growable.performBonemeal((ServerLevel) world, world.random, target, state);
					state = world.getBlockState(target);
				}
				world.levelEvent(2005, target, 0);
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
}
