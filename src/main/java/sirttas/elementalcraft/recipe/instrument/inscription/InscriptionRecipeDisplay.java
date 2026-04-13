package sirttas.elementalcraft.recipe.instrument.inscription;

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

public record InscriptionRecipeDisplay(
        ElementType elementType,
        int elementAmount,
        List<SlotDisplay> ingredients,
        SlotDisplay result,
        SlotDisplay craftingStation
) implements RecipeDisplay {

    public static final MapCodec<InscriptionRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            ElementType.MAP_CODEC.forGetter(InscriptionRecipeDisplay::elementType),
            Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(InscriptionRecipeDisplay::elementAmount),
            SlotDisplay.CODEC.listOf().fieldOf(ECNames.INGREDIENTS).forGetter(InscriptionRecipeDisplay::ingredients),
            SlotDisplay.CODEC.fieldOf(ECNames.RESULT).forGetter(InscriptionRecipeDisplay::result),
            SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(InscriptionRecipeDisplay::craftingStation)
    ).apply(builder, InscriptionRecipeDisplay::new));
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull InscriptionRecipeDisplay> STREAM_CODEC =  StreamCodec.composite(
            ElementType.STREAM_CODEC, InscriptionRecipeDisplay::elementType,
            ByteBufCodecs.INT, InscriptionRecipeDisplay::elementAmount,
            SlotDisplay.STREAM_CODEC.apply(ByteBufCodecs.list()), InscriptionRecipeDisplay::ingredients,
            SlotDisplay.STREAM_CODEC, InscriptionRecipeDisplay::result,
            SlotDisplay.STREAM_CODEC, InscriptionRecipeDisplay::craftingStation,
            InscriptionRecipeDisplay::new);
    public static final Type<@NotNull InscriptionRecipeDisplay> TYPE = new Type<>(MAP_CODEC, STREAM_CODEC);

    @Override
    @NotNull
    public Type<? extends @NotNull RecipeDisplay> type() {
        return TYPE;
    }
}
