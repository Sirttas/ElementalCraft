package sirttas.elementalcraft.spell.water;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellCastResult;

import javax.annotation.Nonnull;

public class AnimalGrowthSpell extends Spell {

	public static final String NAME = "animal_growth";

	public AnimalGrowthSpell(ResourceKey<Spell> key) {
		super(key);
	}

	@Nonnull
	@Override
	public SpellCastResult castOnEntity(@Nonnull Level level, @Nonnull Entity caster, @Nonnull Entity target) {
		if (target instanceof Animal animal && animal.isBaby()) {
			animal.setAge(0);
			return SpellCastResult.SUCCESS;
		}
		return SpellCastResult.PASS;
	}


}
