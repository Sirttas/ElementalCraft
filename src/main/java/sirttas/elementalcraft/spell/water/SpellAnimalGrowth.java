package sirttas.elementalcraft.spell.water;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.ActionResultType;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.spell.IEntityCastedSpell;
import sirttas.elementalcraft.spell.Spell;

public class SpellAnimalGrowth extends Spell implements IEntityCastedSpell {

	public static final String NAME = "animal_growth";

	public SpellAnimalGrowth() {
		super(Properties.create(Spell.Type.UTILITY).elementType(ElementType.WATER).cooldown(ECConfig.CONFIG.animalGrowthCooldown.get()).consumeAmount(ECConfig.CONFIG.animalGrowthConsumeAmount.get()));
	}


	@Override
	public ActionResultType castOnEntity(Entity sender, Entity target) {
		if (target instanceof AnimalEntity && ((AnimalEntity) target).isChild()) {
			((AnimalEntity) target).setGrowingAge(0);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}


}
