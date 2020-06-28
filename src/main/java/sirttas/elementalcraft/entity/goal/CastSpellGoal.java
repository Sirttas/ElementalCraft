package sirttas.elementalcraft.entity.goal;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.ActionResultType;
import sirttas.elementalcraft.spell.IEntityCastedSpell;
import sirttas.elementalcraft.spell.Spell;

public class CastSpellGoal extends Goal {

	private CreatureEntity caster;
	private Spell spell;
	private ActionResultType lasCastResult;

	public CastSpellGoal(CreatureEntity caster, Spell spell) {
		this.caster = caster;
		this.spell = spell;
		this.lasCastResult = ActionResultType.PASS;
	}

	private void cast() {
		if (this.spell instanceof IEntityCastedSpell) {
			lasCastResult = ((IEntityCastedSpell) this.spell).castOnEntity(caster, caster.getAttackTarget());
		}
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
		return caster.getAttackTarget() != null;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.shouldExecute() && lasCastResult == ActionResultType.SUCCESS;
	}
}
