package sirttas.elementalcraft.entity.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.InteractionResult;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellTickManager;

public class CastSpellGoal extends Goal {

	private PathfinderMob caster;
	private Spell spell;

	public CastSpellGoal(PathfinderMob caster, Spell spell) {
		this.caster = caster;
		this.spell = spell;
	}

	private void cast() {
		InteractionResult result = this.spell.castOnEntity(caster, caster.getTarget());

		if (!result.consumesAction()) {
			result = this.spell.castOnSelf(caster);
		}
		if (result.consumesAction()) {
			this.spell.consume(caster, false);
			SpellTickManager.getInstance(caster.getCommandSenderWorld()).setCooldown(caster, spell);
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
