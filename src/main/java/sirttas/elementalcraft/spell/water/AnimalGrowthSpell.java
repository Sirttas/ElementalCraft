package sirttas.elementalcraft.spell.water;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellCastResult;
import sirttas.elementalcraft.spell.properties.SpellProperties;

public class AnimalGrowthSpell extends Spell {

	public static final String NAME = "animal_growth";

	public AnimalGrowthSpell(Holder<SpellProperties> properties) {
		super(properties);
	}

	@Override
	public SpellCastResult castOnEntity(Level level, Entity caster, Entity target) {
		if (target instanceof Animal animal && animal.isBaby()) {
			animal.setAge(0);
			return SpellCastResult.SUCCESS;
		}
		return SpellCastResult.PASS;
	}


}
