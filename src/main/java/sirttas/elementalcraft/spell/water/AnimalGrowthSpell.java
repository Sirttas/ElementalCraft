package sirttas.elementalcraft.spell.water;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import sirttas.elementalcraft.spell.Spell;

public class AnimalGrowthSpell extends Spell {

	public static final String NAME = "animal_growth";

	public AnimalGrowthSpell(ResourceKey<Spell> key) {
		super(key);
	}

	@Override
	public InteractionResult castOnEntity(Entity sender, Entity target) {
		if (target instanceof Animal && ((Animal) target).isBaby()) {
			((Animal) target).setAge(0);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}


}
