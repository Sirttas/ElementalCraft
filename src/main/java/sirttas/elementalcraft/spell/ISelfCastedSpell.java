package sirttas.elementalcraft.spell;

import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResultType;

public interface ISelfCastedSpell {


	ActionResultType castOnSelf(Entity sender);
}
