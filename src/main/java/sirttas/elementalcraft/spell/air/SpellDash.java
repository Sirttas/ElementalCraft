package sirttas.elementalcraft.spell.air;

import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResultType;
import sirttas.elementalcraft.spell.Spell;

public class SpellDash extends Spell {

	public static final String NAME = "dash";

	@Override
	public ActionResultType castOnSelf(Entity sender) {
		sender.setMotion(sender.getMotion().add(sender.getLookVec().normalize().scale(getRange())));
		return ActionResultType.SUCCESS;
	}
}
