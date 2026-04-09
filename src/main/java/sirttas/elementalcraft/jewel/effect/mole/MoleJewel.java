package sirttas.elementalcraft.jewel.effect.mole;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.jewel.effect.EffectJewel;

import java.util.function.Consumer;

public class MoleJewel extends EffectJewel {

    public static final String NAME = "mole";

    public MoleJewel() {
        super(ElementType.EARTH, 1000, false, new MobEffectInstance(MobEffects.DIG_SPEED, 1200, 2));
    }

    @Override
    public void appendHoverText(@NotNull Consumer<Component> builder) {
        builder.accept(Component.translatable("tooltip.elementalcraft.mole").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(builder);
    }
}
