package sirttas.elementalcraft.spell.water;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.ActionResultType;
import sirttas.elementalcraft.spell.Spell;

public class AnimalGrowthSpell extends Spell {

	public static final String NAME = "animal_growth";

	@Override
	public ActionResultType castOnEntity(Entity sender, Entity target) {
		if (target instanceof AnimalEntity && ((AnimalEntity) target).isBaby()) {
			((AnimalEntity) target).setAge(0);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}


}
