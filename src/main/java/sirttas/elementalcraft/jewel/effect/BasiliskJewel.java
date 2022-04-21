package sirttas.elementalcraft.jewel.effect;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.entity.EntityHelper;

import java.util.List;

public class BasiliskJewel extends EffectJewel {

    public static final String NAME = "basilisk";

    public BasiliskJewel() {
        super(ElementType.WATER, 20,
                new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, 3),
                new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 2, 2));
    }

    private Entity getTarget(Entity entity) {
        var hit = EntityHelper.rayTrace(entity);

        if (hit.getType() == HitResult.Type.ENTITY && hit instanceof EntityHitResult entityHitResult) {
            return entityHitResult.getEntity();
        }
        return null;
    }

    @Override
    public boolean isActive(Entity entity) {
        if (!super.isActive(entity)) {
            return false;
        }

        var target = getTarget(entity);

        if (target == null) {
            return false;
        }
        return !entity.isAlliedTo(target) && target instanceof LivingEntity livingEntity && this.effects.stream().allMatch(livingEntity::canBeAffected);
    }

    @Override
    public void apply(Entity entity) {
        var target = getTarget(entity);

        if (target != null) {
            super.apply(target);
        }
    }

    @Override
    public void appendHoverText(List<Component> tooltip) {
        tooltip.add(new TranslatableComponent("tooltip.elementalcraft.basilisk").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(tooltip);
    }


}
