package sirttas.elementalcraft.spell;

import net.minecraft.world.entity.Entity;

public abstract class AbstractSpellInstance {

	private final Spell spell;
	private final Entity caster;
	private final int duration;
	private int remainingTicks;

	protected AbstractSpellInstance(Entity caster, Spell spell) {
		this(caster, spell, spell.getCooldown());
	}

	protected AbstractSpellInstance(Entity caster, Spell spell, int duration) {
		this.caster = caster;
		this.spell = spell;
		this.duration = duration;
		this.remainingTicks = duration;
	}

	public abstract void tick();

	public void end() {
		this.remainingTicks = -1;
	}

	void decTick() {
		remainingTicks--;
	}

	public boolean isFinished() {
		return remainingTicks < 0 || caster.isRemoved();
	}

	public int getTicks() {
		return duration - remainingTicks;
	}

	public static AbstractSpellInstance delay(Entity sender, Spell spell, int delay, Runnable cast) {
		return new Delay(sender, spell, delay, cast);
	}

	public Spell getSpell() {
		return spell;
	}

	public Entity getCaster() {
		return caster;
	}

	public int getDuration() {
		return duration;
	}

	private static class Delay extends AbstractSpellInstance {

		private final Runnable cast;

		public Delay(Entity sender, Spell spell, int delay, Runnable cast) {
			super(sender, spell, delay);
			this.cast = cast;
		}

		@Override
		public void tick() {
			if (getTicks() == getDuration()) {
				cast.run();
			}
		}
	}
}
