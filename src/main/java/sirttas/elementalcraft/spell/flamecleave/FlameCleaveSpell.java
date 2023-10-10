package sirttas.elementalcraft.spell.flamecleave;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.LevelEvent;
import sirttas.elementalcraft.spell.AoeSpell;
import sirttas.elementalcraft.spell.Spell;

import javax.annotation.Nonnull;

public class FlameCleaveSpell extends AoeSpell {

	public static final String NAME = "flame_cleave";

	public FlameCleaveSpell(ResourceKey<Spell> key) {
		super(key);
	}

	@Override
	public @Nonnull InteractionResult castOnSelf(@Nonnull Entity caster) {
		if (caster instanceof LivingEntity) {
			this.delay(caster, 10, () -> {
				var value = super.castOnSelf(caster);

				if (value == InteractionResult.SUCCESS) {
					caster.level().levelEvent(null, LevelEvent.PARTICLES_MOBBLOCK_SPAWN, caster.blockPosition(), 0);
				}
			});
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	protected void onHit(LivingEntity sender, LivingEntity target, float damage) {
		target.setSecondsOnFire(5);
	}
}
