package sirttas.elementalcraft.spell.air;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import sirttas.elementalcraft.spell.Spell;

public class DashSpell extends Spell {

	public static final String NAME = "dash";

	public DashSpell(ResourceKey<Spell> key) {
		super(key);
	}

	@Override
	public InteractionResult castOnSelf(Entity sender) {
		sender.setDeltaMovement(sender.getDeltaMovement().add(sender.getLookAngle().normalize().scale(getRange(sender))));
		return InteractionResult.SUCCESS;
	}
}
