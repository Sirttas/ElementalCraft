package sirttas.elementalcraft.entity.goal;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.Goal;
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
		this.spell.castOnEntity(caster, caster.getAttackTarget());
	}

	@Override
	public void startExecuting() {
		cast();
	}

	@Override
	public void tick() {
		cast();
	}

	@Override
	public boolean shouldExecute() {
		return caster.getAttackTarget() != null && !SpellTickManager.getInstance(caster.world).hasCooldown(caster, spell);
	}

}
