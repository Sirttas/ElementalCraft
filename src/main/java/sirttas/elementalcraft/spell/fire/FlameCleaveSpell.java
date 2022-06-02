package sirttas.elementalcraft.spell.fire;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.LevelEvent;
import sirttas.elementalcraft.spell.AoeSpell;
import sirttas.elementalcraft.spell.Spell;

public class FlameCleaveSpell extends AoeSpell {

	public static final String NAME = "flame_cleave";

	public FlameCleaveSpell(ResourceKey<Spell> key) {
		super(key);
	}

	@Override
	public InteractionResult castOnSelf(Entity sender) {
		var value = super.castOnSelf(sender);

		if (value == InteractionResult.SUCCESS) {
			sender.level.levelEvent(null, LevelEvent.PARTICLES_MOBBLOCK_SPAWN, sender.blockPosition(), 0);
		}
		return value;
	}

	@Override
	protected void onHit(LivingEntity sender, LivingEntity target, float damage) {
		target.setSecondsOnFire(5);
	}
}
