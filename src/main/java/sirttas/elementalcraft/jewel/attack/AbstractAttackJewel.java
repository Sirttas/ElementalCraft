package sirttas.elementalcraft.jewel.attack;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.jewel.Jewel;

public abstract class AbstractAttackJewel extends Jewel {

    protected AbstractAttackJewel(ElementType elementType, int consumption, boolean ticking) {
        super(elementType, consumption, ticking);
    }

    public abstract void onAttack(Entity attacker, LivingEntity target);
}
