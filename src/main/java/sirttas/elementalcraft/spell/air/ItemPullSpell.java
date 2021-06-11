package sirttas.elementalcraft.spell.air;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.spell.Spell;

public class ItemPullSpell extends Spell {

	public static final String NAME = "item_pull";

	@Override
	public ActionResultType castOnSelf(Entity sender) {
		Vector3d pos = sender.position();
		World world = sender.getCommandSenderWorld();

		world.getEntitiesOfClass(ItemEntity.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)).inflate(getRange(sender))).stream().forEach(i -> {
			if (world.isClientSide) {
				ParticleHelper.createEnderParticle(world, i.position(), 3, world.random);
			}
			i.setPos(pos.x, pos.y, pos.z);
		});
		return ActionResultType.SUCCESS;
	}
}
