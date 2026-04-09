package sirttas.elementalcraft.jewel.effect;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class PhoenixJewel extends EffectJewel {

    public static final String NAME = "phoenix";

    public PhoenixJewel() {
        super(ElementType.FIRE, 20,true,
                new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2),
                new MobEffectInstance(MobEffects.REGENERATION, 2));
    }

    @Override
    public boolean isActive(@Nonnull Entity entity, @Nullable IElementStorage elementStorage) {
        return entity.isOnFire() && super.isActive(entity, elementStorage);
    }

    @Override
    public void appendHoverText(@NotNull Consumer<Component> builder) {
        builder.accept(Component.translatable("tooltip.elementalcraft.phoenix").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(builder);
    }

}
