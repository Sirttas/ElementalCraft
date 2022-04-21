package sirttas.elementalcraft.jewel.defence;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.jewel.Jewel;

public class DefenceJewel extends Jewel {

    protected DefenceJewel(ElementType elementType, int consumption) {
        super(elementType, consumption);
        this.ticking = false;
    }

    public float onHurt(Entity entity, DamageSource source, float amount) {
        return amount;
    }
}
