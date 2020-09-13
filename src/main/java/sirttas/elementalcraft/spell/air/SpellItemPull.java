package sirttas.elementalcraft.spell.air;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import sirttas.elementalcraft.spell.ISelfCastedSpell;
import sirttas.elementalcraft.spell.Spell;

public class SpellItemPull extends Spell implements ISelfCastedSpell {

	public static final String NAME = "item_pull";

	@Override
	public ActionResultType castOnSelf(Entity sender) {
		Vec3d pos = sender.getPositionVector();
		sender.getEntityWorld().getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)).grow(10)).stream().forEach(i -> i.setPosition(pos.x, pos.y, pos.z));
		return ActionResultType.SUCCESS;
	}

	@Override
	public boolean consume(Entity sender) { // NOSONAR
		return false;
	}
}
