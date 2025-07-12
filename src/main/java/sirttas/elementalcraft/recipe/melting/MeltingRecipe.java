package sirttas.elementalcraft.recipe.melting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.IECRecipe;

public record MeltingRecipe(
        HolderSet<Block> input,
        Fluid result,
        int cooldown,
        int elementAmount,
        float fillingAmount
) implements IECRecipe<MeltingRecipeInput> {

    public static final String NAME = "melting";

    @Override
    public boolean matches(@NotNull MeltingRecipeInput recipeInput, @NotNull Level level) {
        return recipeInput.elementAmount() >= elementAmount * recipeInput.elementConsumption() && recipeInput.state().is(input);
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ECRecipeSerializers.MELTING.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ECRecipeTypes.MELTING.get();
    }

    public static class Serializer implements RecipeSerializer<MeltingRecipe> {

        public static final MapCodec<MeltingRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
                RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf(ECNames.INPUT).forGetter(MeltingRecipe::input),
                BuiltInRegistries.FLUID.byNameCodec().optionalFieldOf(ECNames.RESULT, Fluids.EMPTY).forGetter(MeltingRecipe::result),
                Codec.INT.fieldOf("cooldown").forGetter(MeltingRecipe::cooldown),
                Codec.INT.fieldOf("element_amount").forGetter(MeltingRecipe::elementAmount),
                Codec.FLOAT.optionalFieldOf("filling_amount", 1000F).forGetter(MeltingRecipe::fillingAmount)
        ).apply(builder, MeltingRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, MeltingRecipe> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.holderSet(Registries.BLOCK), MeltingRecipe::input,
                ByteBufCodecs.registry(Registries.FLUID), MeltingRecipe::result,
                ByteBufCodecs.INT, MeltingRecipe::cooldown,
                ByteBufCodecs.INT, MeltingRecipe::elementAmount,
                ByteBufCodecs.FLOAT, MeltingRecipe::fillingAmount,
                MeltingRecipe::new);


        @Override
        public @NotNull MapCodec<MeltingRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, MeltingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

}
