package sirttas.elementalcraft.jewel.attack;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import sirttas.elementalcraft.api.element.ElementType;

import java.util.List;

public class ViperJewel extends AbstractAttackJewel {

    public static final String NAME = "viper";

    public ViperJewel() {
        super(ElementType.WATER, 1000);
    }

    @Override
    public void appendHoverText(List<Component> tooltip) {
        tooltip.add(new TranslatableComponent("tooltip.elementalcraft.viper").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(tooltip);
    }

    @Override
    public void onAttack(Entity attacker, LivingEntity target) {
        target.addEffect(new MobEffectInstance(MobEffects.POISON, 200));
    }
}
