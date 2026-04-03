package sirttas.elementalcraft.recipe.instrument.io;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;

import javax.annotation.Nonnull;

public abstract class AbstractIOInstrumentRecipe implements IOInstrumentRecipe<SimpleIOInstrumentRecipeInput> {

    protected final CommonInfo commonInfo;
    protected final int elementAmount;
    protected final double luckRatio;
    protected final Ingredient input;
    protected final int inputSize;
    protected final ItemStackTemplate result;

    protected AbstractIOInstrumentRecipe(CommonInfo commonInfo, int elementAmount, double luckRatio, Ingredient input, int inputSize, ItemStackTemplate result) {
        this.commonInfo = commonInfo;
        this.elementAmount = elementAmount;
        this.luckRatio = luckRatio;
        this.input = input;
        this.inputSize = inputSize;
        this.result = result;
    }

    public static <T extends AbstractIOInstrumentRecipe> MapCodec<T> codec(AbstractIOInstrumentRecipe.Factory<T> factory) {
        return RecordCodecBuilder.mapCodec(builder -> builder.group(
                CommonInfo.MAP_CODEC.forGetter(r -> r.commonInfo),
                Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(AbstractIOInstrumentRecipe::getElementAmount),
                Codec.DOUBLE.optionalFieldOf(ECNames.LUCK_RATIO, 0D).forGetter(r -> r.luckRatio),
                Ingredient.CODEC.fieldOf(ECNames.INPUT).forGetter(r -> r.input),
                Codec.INT.optionalFieldOf("input_size", 1).forGetter(AbstractIOInstrumentRecipe::getInputSize),
                ItemStackTemplate.CODEC.fieldOf(ECNames.RESULT).forGetter(r -> r.result)
        ).apply(builder, factory::create));
    }

    public static <T extends AbstractIOInstrumentRecipe> StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull T> streamCodec(AbstractIOInstrumentRecipe.Factory<T> factory) {
        return StreamCodec.composite(
                CommonInfo.STREAM_CODEC, r -> r.commonInfo,
                ByteBufCodecs.INT, AbstractIOInstrumentRecipe::getElementAmount,
                ByteBufCodecs.DOUBLE, r -> r.luckRatio,
                Ingredient.CONTENTS_STREAM_CODEC, r -> r.input,
                ByteBufCodecs.INT, r -> r.inputSize,
                ItemStackTemplate.STREAM_CODEC, r -> r.result,
                factory::create);
    }

    @Override
    public int getElementAmount() {
        return elementAmount;
    }

    @Override
    public int getInputSize() {
        return inputSize;
    }

    @Override
    public boolean matches(@NotNull ItemStack stack, @Nonnull Level level) {
        return input.test(stack) && IOInstrumentRecipe.super.matches(stack, level);
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SimpleIOInstrumentRecipeInput simpleIOInstrumentRecipeInput) {
        return result.create();
    }

    @Override
    public boolean showNotification() {
        return commonInfo.showNotification();
    }

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.create(input);
    }

    @Override
    public int getLuck(SimpleIOInstrumentRecipeInput input) {
        return (int) Math.round(input.getRuneBonus(Rune.BonusType.LUCK) * luckRatio);
    }

    @FunctionalInterface
    public interface Factory<T extends AbstractIOInstrumentRecipe> {
        T create(CommonInfo commonInfo, int elementAmount, double luckRatio, Ingredient input, int inputSize, ItemStackTemplate output);
    }
}
