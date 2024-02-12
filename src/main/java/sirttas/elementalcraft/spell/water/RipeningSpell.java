package sirttas.elementalcraft.spell.water;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.phys.BlockHitResult;
import sirttas.elementalcraft.spell.Spell;

import javax.annotation.Nonnull;

public class RipeningSpell extends Spell {

	public static final String NAME = "ripening";

	public RipeningSpell(ResourceKey<Spell> key) {
		super(key);
	}

	@Nonnull
	@Override
	public InteractionResult castOnBlock(@Nonnull Entity sender, @Nonnull BlockPos target, @Nonnull BlockHitResult hitResult) {
		var level = sender.level();
		var state = level.getBlockState(target);
		var block = state.getBlock();

		if (block instanceof BonemealableBlock growable && growable.isBonemealSuccess(level, level.random, target, state)) {
			if (level instanceof ServerLevel serverLevel) {
				for (int i = 0; i < 10 && growable.isValidBonemealTarget(level, target, state); i++) {
					growable.performBonemeal(serverLevel, level.random, target, state);
					state = level.getBlockState(target);
				}
				level.levelEvent(LevelEvent.PARTICLES_PLANT_GROWTH, target, 0);
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
}
