package sirttas.elementalcraft.entity.goal;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.ActionResultType;
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
		ActionResultType result = this.spell.castOnEntity(caster, caster.getAttackTarget());

		if (!result.isSuccessOrConsume()) {
			result = this.spell.castOnSelf(caster);
		}
		if (result.isSuccessOrConsume()) {
			this.spell.consume(caster);
			SpellTickManager.getInstance(caster.getEntityWorld()).setCooldown(caster, spell);
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
		return caster.getAttackTarget() != null && !SpellTickManager.getInstance(caster.world).hasCooldown(caster, spell);
	}

}
