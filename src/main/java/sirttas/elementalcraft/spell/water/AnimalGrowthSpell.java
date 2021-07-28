package sirttas.elementalcraft.spell.water;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.InteractionResult;
import sirttas.elementalcraft.spell.Spell;

public class AnimalGrowthSpell extends Spell {

	public static final String NAME = "animal_growth";

	@Override
	public InteractionResult castOnEntity(Entity sender, Entity target) {
		if (target instanceof Animal && ((Animal) target).isBaby()) {
			((Animal) target).setAge(0);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}


}
