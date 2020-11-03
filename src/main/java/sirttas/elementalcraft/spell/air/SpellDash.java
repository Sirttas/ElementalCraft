package sirttas.elementalcraft.spell.air;

import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResultType;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.spell.Spell;

public class SpellDash extends Spell {

	public static final String NAME = "dash";

	public SpellDash() {
		super(Properties.create(Spell.Type.MIXED).elementType(ElementType.AIR).cooldown(ECConfig.COMMON.dashCooldown.get()).consumeAmount(ECConfig.COMMON.dashConsumeAmount.get()));
	}

	@Override
	public ActionResultType castOnSelf(Entity sender) {
		sender.setMotion(sender.getMotion().add(sender.getLookVec().normalize().scale(ECConfig.COMMON.dashRange.get())));
		return ActionResultType.SUCCESS;
	}
}
