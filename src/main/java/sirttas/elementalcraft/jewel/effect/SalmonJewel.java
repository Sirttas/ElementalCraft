package sirttas.elementalcraft.jewel.effect;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class SalmonJewel extends EffectJewel {

    public static final String NAME = "salmon";

    public SalmonJewel() {
        super(ElementType.WATER,10, true, new MobEffectInstance(MobEffects.WATER_BREATHING, 2));
    }

    @Override
    public boolean isActive(@Nonnull Entity entity, @Nullable IElementStorage elementStorage) {
        return entity.isEyeInFluid(Tags.Fluids.WATER)
                && !entity.level().getBlockState(BlockPos.containing(entity.getX(), entity.getEyeY(), entity.getZ())).is(Blocks.BUBBLE_COLUMN)
                && super.isActive(entity, elementStorage);
    }

    @Override
    public void appendHoverText(@NotNull Consumer<Component> builder) {
        builder.accept(Component.translatable("tooltip.elementalcraft.salmon").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(builder);
    }
}
