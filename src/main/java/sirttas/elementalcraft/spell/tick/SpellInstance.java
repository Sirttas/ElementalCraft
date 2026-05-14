package sirttas.elementalcraft.spell.tick;

import net.minecraft.core.Holder;
import net.minecraft.core.TypedInstance;
import net.minecraft.world.entity.Entity;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.Spells;

import java.util.function.Consumer;

public abstract class SpellInstance  implements TypedInstance<Spell> {

	private final Holder<Spell> spell;
	private final Entity caster;
	private final int duration;
	private int remainingTicks;

	protected SpellInstance(Entity caster, Holder<Spell> spell) {
		this(caster, spell, spell.value().getCooldown());
	}

	protected SpellInstance(Entity caster, Spell spell, int duration) {
		this(caster, Spells.REGISTRY.wrapAsHolder(spell), duration);
	}

	protected SpellInstance(Entity caster, Holder<Spell> spell, int duration) {
		this.caster = caster;
		this.spell = spell;
		this.duration = duration;
		this.remainingTicks = duration;
	}

	@Override
	public Holder<Spell> typeHolder() {
		return spell;
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


	public static SpellInstance delay(Entity sender, Spell spell, int delay, Runnable cast) {
		return new Delay(sender, spell, delay, cast);
	}

	public static SpellInstance effect(Entity sender, Spell spell, int duration, Consumer<SpellInstance> tick) {
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

	private static class Delay extends SpellInstance {

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

	private static class Effect extends SpellInstance {
		private final Consumer<SpellInstance> ticker;

		public Effect(Entity sender, Spell spell, int duration, Consumer<SpellInstance> ticker) {
			super(sender, spell, duration);
			this.ticker = ticker;
		}

		@Override
		public void tick() {
			ticker.accept(this);
		}
	}
}
