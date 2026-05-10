package sirttas.elementalcraft.spell.flamecleave;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import sirttas.elementalcraft.spell.AoeSpell;
import sirttas.elementalcraft.spell.SpellCastResult;
import sirttas.elementalcraft.spell.properties.SpellProperties;

public class FlameCleaveSpell extends AoeSpell {

	public static final String NAME = "flame_cleave";

	public FlameCleaveSpell(Holder<SpellProperties> properties) {
		super(properties);
	}

	@Override
	public SpellCastResult castOnSelf(Level level, Entity caster) {
		if (caster instanceof LivingEntity) {
			this.delay(caster, 10, () -> {
				var value = super.castOnSelf(level, caster);

				if (value.success()) {
					level.levelEvent(null, LevelEvent.PARTICLES_MOBBLOCK_SPAWN, caster.blockPosition(), 0);
				}
			});
			return SpellCastResult.SUCCESS;
		}
		return SpellCastResult.PASS;
	}

	@Override
	protected void onHit(LivingEntity sender, LivingEntity target, float damage) {
		target.igniteForSeconds(5);
	}
}
