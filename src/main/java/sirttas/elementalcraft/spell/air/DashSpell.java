package sirttas.elementalcraft.spell.air;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import sirttas.elementalcraft.spell.Spell;

import javax.annotation.Nonnull;

public class DashSpell extends Spell {

	public static final String NAME = "dash";

	public DashSpell(ResourceKey<Spell> key) {
		super(key);
	}

	@Override
	public @Nonnull InteractionResult castOnSelf(@Nonnull Entity caster) {
		caster.setDeltaMovement(caster.getDeltaMovement().add(caster.getLookAngle().normalize().scale(getRange(caster))));
		return InteractionResult.SUCCESS;
	}
}
