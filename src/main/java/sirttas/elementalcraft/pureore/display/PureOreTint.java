package sirttas.elementalcraft.pureore.display;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record PureOreTint(int index) implements ItemTintSource {

    public static final MapCodec<PureOreTint> MAP_CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.INT.fieldOf("index").forGetter(PureOreTint::index)
    ).apply(builder, PureOreTint::new));

    @Override
    public int calculate(@NonNull ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        var colors = PureOreDisplayManager.getInstance().getColors(itemStack);

        return colors != null && index < colors.length ? colors[index] : -1;
    }

    @Override
    public @NonNull MapCodec<PureOreTint> type() {
        return MAP_CODEC;
    }
}
