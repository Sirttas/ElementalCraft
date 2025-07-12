package sirttas.elementalcraft.recipe.cracking;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.name.ECNames;

public class CrackingRecipeSerializer<T extends AbstractCrackingRecipe> implements RecipeSerializer<T> {

    private final MapCodec<T> codec;
    private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

    public CrackingRecipeSerializer(AbstractCrackingRecipe.Factory<T> factory) {
        codec = RecordCodecBuilder.mapCodec(builder -> builder.group(
                RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf(ECNames.INPUT).forGetter(AbstractCrackingRecipe::input),
                BuiltInRegistries.BLOCK.byNameCodec().optionalFieldOf(ECNames.RESULT, Blocks.AIR).forGetter(AbstractCrackingRecipe::result),
                Codec.INT.optionalFieldOf(ECNames.ELEMENT_AMOUNT, 0).forGetter(AbstractCrackingRecipe::elementAmount)
        ).apply(builder, factory::create));
        streamCodec = StreamCodec.composite(
                ByteBufCodecs.holderSet(Registries.BLOCK), AbstractCrackingRecipe::input,
                ByteBufCodecs.registry(Registries.BLOCK), AbstractCrackingRecipe::result,
                ByteBufCodecs.INT.apply(i -> i), AbstractCrackingRecipe::elementAmount,
                factory::create);
    }

    @Override
    public @NotNull MapCodec<T> codec() {
        return codec;
    }

    @Override
    public @NotNull StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
        return streamCodec;
    }
}
