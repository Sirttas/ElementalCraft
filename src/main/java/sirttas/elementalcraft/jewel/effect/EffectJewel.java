package sirttas.elementalcraft.jewel.effect;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.jewel.Jewel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class EffectJewel extends Jewel {

    protected final List<MobEffectInstance> effects;

    public EffectJewel(ElementType elementType, int consumption, boolean ticking, MobEffectInstance... effects) {
        super(elementType, consumption, ticking);
        this.effects = ImmutableList.copyOf(effects);
    }

    public void apply(LivingEntity entity) {
        for (MobEffectInstance effect : effects) {
            entity.addEffect(new MobEffectInstance(effect));
        }
    }

    @Override
    public boolean isActive(@Nonnull Entity entity, @Nullable IElementStorage elementStorage) {
        return isActive(entity, entity, elementStorage);
    }

    public boolean isActive(@Nonnull Entity entity, @Nonnull Entity target, @Nullable IElementStorage elementStorage) {
        if (target instanceof LivingEntity livingTarget) {
            var activeEffects = livingTarget.getActiveEffects();

            if (effects.stream().allMatch(effect -> activeEffects.stream().anyMatch(activeEffect -> activeEffect.getEffect().equals(effect.getEffect()) && activeEffect.getAmplifier() >= effect.getAmplifier() && activeEffect.getDuration() >= 2))) {
                return false;
            }
        }
        return super.isActive(entity, elementStorage);
    }
}
