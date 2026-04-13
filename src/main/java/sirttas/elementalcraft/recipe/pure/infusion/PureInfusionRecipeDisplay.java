package sirttas.elementalcraft.recipe.pure.infusion;

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
import sirttas.elementalcraft.block.pureinfuser.PureInfuserBlock;

public record PureInfusionRecipeDisplay(
        int elementAmount,
        SlotDisplay pureInfuserInput,
        SlotDisplay fireInput,
        SlotDisplay waterInput,
        SlotDisplay earthInput,
        SlotDisplay airInput,
        SlotDisplay result,
        SlotDisplay craftingStation
) implements RecipeDisplay {

    public static final MapCodec<PureInfusionRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(PureInfusionRecipeDisplay::elementAmount),
            SlotDisplay.CODEC.fieldOf(PureInfuserBlock.NAME).forGetter(PureInfusionRecipeDisplay::pureInfuserInput),
            SlotDisplay.CODEC.fieldOf(ElementType.FIRE.getSerializedName()).forGetter(PureInfusionRecipeDisplay::fireInput),
            SlotDisplay.CODEC.fieldOf(ElementType.WATER.getSerializedName()).forGetter(PureInfusionRecipeDisplay::waterInput),
            SlotDisplay.CODEC.fieldOf(ElementType.EARTH.getSerializedName()).forGetter(PureInfusionRecipeDisplay::earthInput),
            SlotDisplay.CODEC.fieldOf(ElementType.AIR.getSerializedName()).forGetter(PureInfusionRecipeDisplay::airInput),
            SlotDisplay.CODEC.fieldOf(ECNames.RESULT).forGetter(PureInfusionRecipeDisplay::result),
            SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(PureInfusionRecipeDisplay::craftingStation)
    ).apply(builder, PureInfusionRecipeDisplay::new));
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull PureInfusionRecipeDisplay> STREAM_CODEC =  StreamCodec.composite(
            ByteBufCodecs.INT, PureInfusionRecipeDisplay::elementAmount,
            SlotDisplay.STREAM_CODEC, PureInfusionRecipeDisplay::pureInfuserInput,
            SlotDisplay.STREAM_CODEC, PureInfusionRecipeDisplay::fireInput,
            SlotDisplay.STREAM_CODEC, PureInfusionRecipeDisplay::waterInput,
            SlotDisplay.STREAM_CODEC, PureInfusionRecipeDisplay::earthInput,
            SlotDisplay.STREAM_CODEC, PureInfusionRecipeDisplay::airInput,
            SlotDisplay.STREAM_CODEC, PureInfusionRecipeDisplay::result,
            SlotDisplay.STREAM_CODEC, PureInfusionRecipeDisplay::craftingStation,
            PureInfusionRecipeDisplay::new);
    public static final Type<@NotNull PureInfusionRecipeDisplay> TYPE = new Type<>(MAP_CODEC, STREAM_CODEC);

    @Override
    @NotNull
    public Type<? extends @NotNull RecipeDisplay> type() {
        return TYPE;
    }
}
