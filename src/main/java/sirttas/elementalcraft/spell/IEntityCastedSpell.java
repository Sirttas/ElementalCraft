package sirttas.elementalcraft.spell;

import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResultType;

public interface IEntityCastedSpell {

	ActionResultType castOnEntity(Entity sender, Entity target);
}
