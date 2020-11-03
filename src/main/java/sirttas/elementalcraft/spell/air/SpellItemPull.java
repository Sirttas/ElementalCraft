package sirttas.elementalcraft.spell.air;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.spell.Spell;

public class SpellItemPull extends Spell {

	public static final String NAME = "item_pull";

	public SpellItemPull() {
		super(Properties.create(Spell.Type.UTILITY).elementType(ElementType.AIR).cooldown(ECConfig.COMMON.itemPullCooldown.get()).consumeAmount(ECConfig.COMMON.itemPullConsumeAmount.get()));
	}

	@Override
	public ActionResultType castOnSelf(Entity sender) {
		Vector3d pos = sender.getPositionVec();
		World world = sender.getEntityWorld();

		world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)).grow(ECConfig.COMMON.itemPullRange.get())).stream().forEach(i -> {
			if (world.isRemote) {
				ParticleHelper.createEnderParticle(world, i.getPositionVec(), 3, world.rand);
			}
			i.setPosition(pos.x, pos.y, pos.z);
		});
		return ActionResultType.SUCCESS;
	}
}
