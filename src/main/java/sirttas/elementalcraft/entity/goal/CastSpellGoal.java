package sirttas.elementalcraft.entity.goal;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellTickManager;

public class CastSpellGoal extends Goal {

	private CreatureEntity caster;
	private Spell spell;

	public CastSpellGoal(CreatureEntity caster, Spell spell) {
		this.caster = caster;
		this.spell = spell;
	}

	private void cast() {
		ActionResultType result = this.spell.castOnEntity(caster, caster.getTarget());

		if (!result.consumesAction()) {
			result = this.spell.castOnSelf(caster);
		}
		if (result.consumesAction()) {
			World level = caster.getCommandSenderWorld();
			
			this.spell.consume(caster, false);
			if (!level.isClientSide) {
				SpellTickManager.getInstance(caster.getCommandSenderWorld()).setCooldown(caster, spell);
			}
		}
	}

	@Override
	public void start() {
		cast();
	}

	@Override
	public void tick() {
		cast();
	}

	@Override
	public boolean canUse() {
		return caster.getTarget() != null && !SpellTickManager.getInstance(caster.level).hasCooldown(caster, spell);
	}

}
