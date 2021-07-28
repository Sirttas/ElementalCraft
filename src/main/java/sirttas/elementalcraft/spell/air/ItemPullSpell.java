package sirttas.elementalcraft.spell.air;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.spell.Spell;

public class ItemPullSpell extends Spell {

	public static final String NAME = "item_pull";

	@Override
	public InteractionResult castOnSelf(Entity sender) {
		Vec3 pos = sender.position();
		Level world = sender.getCommandSenderWorld();

		world.getEntitiesOfClass(ItemEntity.class, new AABB(pos, pos.add(1, 1, 1)).inflate(getRange(sender))).stream().forEach(i -> {
			if (world.isClientSide) {
				ParticleHelper.createEnderParticle(world, i.position(), 3, world.random);
			}
			i.setPos(pos.x, pos.y, pos.z);
		});
		return InteractionResult.SUCCESS;
	}
}
