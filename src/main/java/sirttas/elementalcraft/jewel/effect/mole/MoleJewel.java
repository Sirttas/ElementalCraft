package sirttas.elementalcraft.jewel.effect.mole;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.jewel.effect.EffectJewel;

import java.util.List;

public class MoleJewel extends EffectJewel {

    public static final String NAME = "mole";

    public MoleJewel() {
        super(ElementType.EARTH, 1000, new MobEffectInstance(MobEffects.DIG_SPEED, 1200, 2));
        this.ticking = false;
    }

    @Override
    public void appendHoverText(List<Component> tooltip) {
        tooltip.add(new TranslatableComponent("tooltip.elementalcraft.mole").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(tooltip);
    }
}
