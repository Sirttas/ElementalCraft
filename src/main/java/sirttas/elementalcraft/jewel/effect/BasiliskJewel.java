package sirttas.elementalcraft.jewel.effect;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.CommonHooks;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.entity.EntityHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class BasiliskJewel extends EffectJewel {

    public static final String NAME = "basilisk";

    public BasiliskJewel() {
        super(ElementType.WATER, 20, true,
                new MobEffectInstance(MobEffects.SLOWNESS, 2, 3),
                new MobEffectInstance(MobEffects.MINING_FATIGUE, 2, 2));
    }

    private Entity getTarget(Entity entity) {
        var hit = EntityHelper.rayTrace(entity);

        if (hit.getType() == HitResult.Type.ENTITY && hit instanceof EntityHitResult entityHitResult) {
            return entityHitResult.getEntity();
        }
        return null;
    }

    @Override
    public boolean isActive(@Nonnull Entity entity, @Nullable IElementStorage elementStorage) {
        var target = getTarget(entity);

        if (target == null) {
            return false;
        }
        return !entity.isAlliedTo(target)
                && target instanceof LivingEntity livingTarget
                && this.effects.stream().allMatch(effect -> CommonHooks.canMobEffectBeApplied(livingTarget, effect, entity))
                && super.isActive(entity, target, elementStorage);
    }

    @Override
    public void apply(LivingEntity entity) {
        var target = getTarget(entity);

        if (target instanceof LivingEntity livingEntity) {
            super.apply(livingEntity);
        }
    }

    @Override
    public void appendHoverText(@NotNull Consumer<Component> builder) {
        builder.accept(Component.translatable("tooltip.elementalcraft.basilisk").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(builder);
    }


}
