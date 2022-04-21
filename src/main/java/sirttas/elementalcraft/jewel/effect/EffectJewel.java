package sirttas.elementalcraft.jewel.effect;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.jewel.Jewel;

import java.util.List;

public class EffectJewel extends Jewel {

    protected final List<MobEffectInstance> effects;

    public EffectJewel(ElementType elementType, int consumption, MobEffectInstance... effects) {
        super(elementType, consumption);
        this.effects = ImmutableList.copyOf(effects);
    }

    public void apply(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            for (MobEffectInstance e : effects) {
                var effect = new MobEffectInstance(e);

                effect.setNoCounter(true);
                livingEntity.addEffect(effect);
            }
        }
    }
}
