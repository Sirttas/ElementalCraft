package sirttas.elementalcraft.spell.air;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellCastResult;
import sirttas.elementalcraft.spell.properties.SpellProperties;

public class ItemPullSpell extends Spell {

	public static final String NAME = "item_pull";

	public ItemPullSpell(Holder<SpellProperties> properties) {
		super(properties);
	}

	@Override
	public SpellCastResult castOnSelf(Level level, Entity caster) {
		var pos = caster.position();

		level.getEntitiesOfClass(ItemEntity.class, new AABB(pos, pos.add(1, 1, 1)).inflate(getRange(caster))).forEach(i -> {
			if (level.isClientSide()) {
				ParticleHelper.createEnderParticle(level, i.position(), 3, level.getRandom());
			}
			i.setPos(pos.x, pos.y, pos.z);
		});
		return SpellCastResult.SUCCESS;
	}
}
