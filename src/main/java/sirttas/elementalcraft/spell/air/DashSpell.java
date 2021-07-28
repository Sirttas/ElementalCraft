package sirttas.elementalcraft.spell.air;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionResult;
import sirttas.elementalcraft.spell.Spell;

public class DashSpell extends Spell {

	public static final String NAME = "dash";

	@Override
	public InteractionResult castOnSelf(Entity sender) {
		sender.setDeltaMovement(sender.getDeltaMovement().add(sender.getLookAngle().normalize().scale(getRange(sender))));
		return InteractionResult.SUCCESS;
	}
}
