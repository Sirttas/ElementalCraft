package sirttas.elementalcraft.jewel.effect;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import sirttas.elementalcraft.api.element.ElementType;

import java.util.List;

public class PhoenixJewel extends EffectJewel {

    public static final String NAME = "phoenix";

    public PhoenixJewel() {
        super(ElementType.FIRE, 20,
                new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2),
                new MobEffectInstance(MobEffects.REGENERATION, 2));
    }

    @Override
    public boolean isActive(Entity entity) {
        return entity.isOnFire() && super.isActive(entity);
    }

    @Override
    public void appendHoverText(List<Component> tooltip) {
        tooltip.add(new TranslatableComponent("tooltip.elementalcraft.phoenix").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(tooltip);
    }

}
