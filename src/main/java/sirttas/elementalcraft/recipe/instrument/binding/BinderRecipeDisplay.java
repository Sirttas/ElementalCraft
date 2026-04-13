package sirttas.elementalcraft.recipe.instrument.binding;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;

import java.util.List;

public record BinderRecipeDisplay(
        ElementType elementType,
        int elementAmount,
        List<SlotDisplay> ingredients,
        SlotDisplay result,
        SlotDisplay craftingStation
) implements RecipeDisplay {

    public static final MapCodec<BinderRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            ElementType.MAP_CODEC.forGetter(BinderRecipeDisplay::elementType),
            Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(BinderRecipeDisplay::elementAmount),
            SlotDisplay.CODEC.listOf().fieldOf(ECNames.INGREDIENTS).forGetter(BinderRecipeDisplay::ingredients),
            SlotDisplay.CODEC.fieldOf(ECNames.RESULT).forGetter(BinderRecipeDisplay::result),
            SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(BinderRecipeDisplay::craftingStation)
    ).apply(builder, BinderRecipeDisplay::new));
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull BinderRecipeDisplay> STREAM_CODEC =  StreamCodec.composite(
            ElementType.STREAM_CODEC, BinderRecipeDisplay::elementType,
            ByteBufCodecs.INT, BinderRecipeDisplay::elementAmount,
            SlotDisplay.STREAM_CODEC.apply(ByteBufCodecs.list()), BinderRecipeDisplay::ingredients,
            SlotDisplay.STREAM_CODEC, BinderRecipeDisplay::result,
            SlotDisplay.STREAM_CODEC, BinderRecipeDisplay::craftingStation,
            BinderRecipeDisplay::new);
    public static final Type<@NotNull BinderRecipeDisplay> TYPE = new Type<>(MAP_CODEC, STREAM_CODEC);

    @Override
    @NotNull
    public Type<? extends @NotNull RecipeDisplay> type() {
        return TYPE;
    }
}
