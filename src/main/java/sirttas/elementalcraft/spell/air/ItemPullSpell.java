package sirttas.elementalcraft.spell.air;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.spell.Spell;

import javax.annotation.Nonnull;

public class ItemPullSpell extends Spell {

	public static final String NAME = "item_pull";

	public ItemPullSpell(ResourceKey<Spell> key) {
		super(key);
	}

	@Override
	public @Nonnull InteractionResult castOnSelf(@Nonnull Entity caster) {
		Vec3 pos = caster.position();
		Level world = caster.level();

		world.getEntitiesOfClass(ItemEntity.class, new AABB(pos, pos.add(1, 1, 1)).inflate(getRange(caster))).forEach(i -> {
			if (world.isClientSide) {
				ParticleHelper.createEnderParticle(world, i.position(), 3, world.random);
			}
			i.setPos(pos.x, pos.y, pos.z);
		});
		return InteractionResult.SUCCESS;
	}
}
