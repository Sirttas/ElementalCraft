package sirttas.elementalcraft.recipe.spell;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.name.ECNames;

public record SpellCraftRecipeDisplay(
        SlotDisplay gem,
        SlotDisplay crystal,
        SlotDisplay result,
        SlotDisplay craftingStation
) implements RecipeDisplay {

    public static final MapCodec<SpellCraftRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            SlotDisplay.CODEC.fieldOf(ECNames.GEM).forGetter(SpellCraftRecipeDisplay::gem),
            SlotDisplay.CODEC.fieldOf(ECNames.CRYSTAL).forGetter(SpellCraftRecipeDisplay::crystal),
            SlotDisplay.CODEC.fieldOf(ECNames.RESULT).forGetter(SpellCraftRecipeDisplay::result),
            SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(SpellCraftRecipeDisplay::craftingStation)
    ).apply(builder, SpellCraftRecipeDisplay::new));
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull SpellCraftRecipeDisplay> STREAM_CODEC =  StreamCodec.composite(
            SlotDisplay.STREAM_CODEC, SpellCraftRecipeDisplay::gem,
            SlotDisplay.STREAM_CODEC, SpellCraftRecipeDisplay::crystal,
            SlotDisplay.STREAM_CODEC, SpellCraftRecipeDisplay::result,
            SlotDisplay.STREAM_CODEC, SpellCraftRecipeDisplay::craftingStation,
            SpellCraftRecipeDisplay::new);
    public static final Type<@NotNull SpellCraftRecipeDisplay> TYPE = new Type<>(MAP_CODEC, STREAM_CODEC);

    @Override
    @NotNull
    public Type<? extends @NotNull RecipeDisplay> type() {
        return TYPE;
    }
}
