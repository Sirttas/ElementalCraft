package sirttas.elementalcraft.spell;

import net.minecraft.entity.Entity;

public abstract class AbstractSpellInstance {

	protected final Spell spell;
	protected final Entity sender;
	private int remaingTicks;

	protected AbstractSpellInstance(Entity sender, Spell spell) {
		this.sender = sender;
		this.spell = spell;
		this.remaingTicks = spell.getCooldown();
	}

	public abstract void tick();

	public void end() {
		this.remaingTicks = 0;
	}

	void decTick() {
		remaingTicks--;
	}

	public boolean isFinished() {
		return remaingTicks <= 0;
	}
}
