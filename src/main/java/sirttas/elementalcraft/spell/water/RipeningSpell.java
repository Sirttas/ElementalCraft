package sirttas.elementalcraft.spell.water;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.phys.BlockHitResult;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellCastResult;
import sirttas.elementalcraft.spell.properties.SpellProperties;

public class RipeningSpell extends Spell {

	public static final String NAME = "ripening";

	public RipeningSpell(Holder<SpellProperties> properties) {
		super(properties);
	}

	@Override
	public SpellCastResult castOnBlock(Level level, Entity sender, BlockPos target, BlockHitResult hitResult) {
		var state = level.getBlockState(target);
		var block = state.getBlock();

		if (block instanceof BonemealableBlock growable && growable.isBonemealSuccess(level, level.getRandom(), target, state)) {
			if (level instanceof ServerLevel serverLevel) {
				for (int i = 0; i < 10 && growable.isValidBonemealTarget(level, target, state); i++) {
					growable.performBonemeal(serverLevel, level.getRandom(), target, state);
					state = level.getBlockState(target);
				}
				level.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, target, 0);
			}
			return SpellCastResult.SUCCESS;
		}
		return SpellCastResult.PASS;
	}
}
