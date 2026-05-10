package sirttas.elementalcraft.spell.tick;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.Spells;

import java.util.function.Consumer;

public abstract class AbstractSpellInstance {

	private final Holder<Spell> spell;
	private final Entity caster;
	private final int duration;
	private int remainingTicks;

	protected AbstractSpellInstance(Entity caster, Holder<Spell> spell) {
		this(caster, spell, spell.value().getCooldown());
	}

	protected AbstractSpellInstance(Entity caster, Spell spell, int duration) {
		this(caster, Spells.REGISTRY.wrapAsHolder(spell), duration);
	}

	protected AbstractSpellInstance(Entity caster, Holder<Spell> spell, int duration) {
		this.caster = caster;
		this.spell = spell;
		this.duration = duration;
		this.remainingTicks = duration;
	}

	public abstract void tick();

	public void stop() {
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

	public static AbstractSpellInstance effect(Entity sender, Spell spell, int duration, Consumer<AbstractSpellInstance> tick) {
		return new Effect(sender, spell, duration, tick);
	}

	public Holder<Spell> getSpell() {
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

	private static class Effect extends AbstractSpellInstance {
		private final Consumer<AbstractSpellInstance> ticker;

		public Effect(Entity sender, Spell spell, int duration, Consumer<AbstractSpellInstance> ticker) {
			super(sender, spell, duration);
			this.ticker = ticker;
		}

		@Override
		public void tick() {
			ticker.accept(this);
		}
	}
}
