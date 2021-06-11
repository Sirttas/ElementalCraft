package sirttas.elementalcraft.spell.air;

import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResultType;
import sirttas.elementalcraft.spell.Spell;

public class DashSpell extends Spell {

	public static final String NAME = "dash";

	@Override
	public ActionResultType castOnSelf(Entity sender) {
		sender.setDeltaMovement(sender.getDeltaMovement().add(sender.getLookAngle().normalize().scale(getRange(sender))));
		return ActionResultType.SUCCESS;
	}
}
