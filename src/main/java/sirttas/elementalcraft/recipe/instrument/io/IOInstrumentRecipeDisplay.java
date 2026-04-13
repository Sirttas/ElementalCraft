package sirttas.elementalcraft.recipe.instrument.io;

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

public record IOInstrumentRecipeDisplay(
        ElementType elementType,
        int elementAmount,
        SlotDisplay input,
        SlotDisplay result,
        SlotDisplay craftingStation
) implements RecipeDisplay {

    public static final String NAME = "io_instrument";
    public static final MapCodec<IOInstrumentRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            ElementType.MAP_CODEC.forGetter(IOInstrumentRecipeDisplay::elementType),
            Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(IOInstrumentRecipeDisplay::elementAmount),
            SlotDisplay.CODEC.fieldOf(ECNames.INPUT).forGetter(IOInstrumentRecipeDisplay::input),
            SlotDisplay.CODEC.fieldOf(ECNames.RESULT).forGetter(IOInstrumentRecipeDisplay::result),
            SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(IOInstrumentRecipeDisplay::craftingStation)
    ).apply(builder, IOInstrumentRecipeDisplay::new));
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull IOInstrumentRecipeDisplay> STREAM_CODEC =  StreamCodec.composite(
            ElementType.STREAM_CODEC, IOInstrumentRecipeDisplay::elementType,
            ByteBufCodecs.INT, IOInstrumentRecipeDisplay::elementAmount,
            SlotDisplay.STREAM_CODEC, IOInstrumentRecipeDisplay::input,
            SlotDisplay.STREAM_CODEC, IOInstrumentRecipeDisplay::result,
            SlotDisplay.STREAM_CODEC, IOInstrumentRecipeDisplay::craftingStation,
            IOInstrumentRecipeDisplay::new);
    public static final Type<@NotNull IOInstrumentRecipeDisplay> TYPE = new Type<>(MAP_CODEC, STREAM_CODEC);

    @Override
    @NotNull
    public Type<? extends @NotNull RecipeDisplay> type() {
        return TYPE;
    }
}
