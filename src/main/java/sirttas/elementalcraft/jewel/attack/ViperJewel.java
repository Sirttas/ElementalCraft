package sirttas.elementalcraft.jewel.attack;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;

import java.util.function.Consumer;

public class ViperJewel extends AbstractAttackJewel {

    public static final String NAME = "viper";

    public ViperJewel() {
        super(ElementType.WATER, 1000, false);
    }

    @Override
    public void onAttack(Entity attacker, LivingEntity target) {
        target.addEffect(new MobEffectInstance(MobEffects.POISON, 200));
    }

    @Override
    public void appendHoverText(@NotNull Consumer<Component> builder) {
        builder.accept(Component.translatable("tooltip.elementalcraft.viper").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(builder);
    }
}
