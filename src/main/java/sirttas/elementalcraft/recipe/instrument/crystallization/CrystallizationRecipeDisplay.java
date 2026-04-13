package sirttas.elementalcraft.recipe.instrument.crystallization;

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

public record CrystallizationRecipeDisplay(
        ElementType elementType,
        int elementAmount,
        SlotDisplay gem,
        SlotDisplay crystal,
        SlotDisplay result,
        SlotDisplay craftingStation
) implements RecipeDisplay {

    public static final MapCodec<CrystallizationRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            ElementType.MAP_CODEC.forGetter(CrystallizationRecipeDisplay::elementType),
            Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(CrystallizationRecipeDisplay::elementAmount),
            SlotDisplay.CODEC.fieldOf(ECNames.GEM).forGetter(CrystallizationRecipeDisplay::gem),
            SlotDisplay.CODEC.fieldOf(ECNames.CRYSTAL).forGetter(CrystallizationRecipeDisplay::crystal),
            SlotDisplay.CODEC.fieldOf(ECNames.RESULT).forGetter(CrystallizationRecipeDisplay::result),
            SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(CrystallizationRecipeDisplay::craftingStation)
    ).apply(builder, CrystallizationRecipeDisplay::new));
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull CrystallizationRecipeDisplay> STREAM_CODEC =  StreamCodec.composite(
            ElementType.STREAM_CODEC, CrystallizationRecipeDisplay::elementType,
            ByteBufCodecs.INT, CrystallizationRecipeDisplay::elementAmount,
            SlotDisplay.STREAM_CODEC, CrystallizationRecipeDisplay::gem,
            SlotDisplay.STREAM_CODEC, CrystallizationRecipeDisplay::crystal,
            SlotDisplay.STREAM_CODEC, CrystallizationRecipeDisplay::result,
            SlotDisplay.STREAM_CODEC, CrystallizationRecipeDisplay::craftingStation,
            CrystallizationRecipeDisplay::new);
    public static final Type<@NotNull CrystallizationRecipeDisplay> TYPE = new Type<>(MAP_CODEC, STREAM_CODEC);

    @Override
    @NotNull
    public Type<? extends @NotNull RecipeDisplay> type() {
        return TYPE;
    }
}
