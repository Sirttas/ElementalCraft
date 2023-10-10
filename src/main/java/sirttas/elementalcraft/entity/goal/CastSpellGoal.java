package sirttas.elementalcraft.entity.goal;


import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.tick.SpellTickHelper;

public class CastSpellGoal extends Goal {

	private final PathfinderMob caster;
	private final Spell spell;

	public CastSpellGoal(PathfinderMob caster, Spell spell) {
		this.caster = caster;
		this.spell = spell;
	}

	private void cast() {
		var target = caster.getTarget();

		if (target == null) {
			return;
		}

		var result = this.spell.castOnEntity(caster, target);

		if (!result.consumesAction()) {
			result = this.spell.castOnSelf(caster);
		}
		if (result.consumesAction()) {
			var level = caster.level();
			
			this.spell.consume(caster, false);
			if (!level.isClientSide) {
				SpellTickHelper.startCooldown(caster, spell);
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
		return caster.getTarget() != null && SpellTickHelper.hasCooldown(caster, spell);
	}

}
