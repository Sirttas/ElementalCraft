package sirttas.elementalcraft.recipe.cracking;

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
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.name.ECNames;

public abstract class AbstractCrackingRecipe implements Recipe<CrackingRecipeInput> {

    private final HolderSet<Block> input;
    private final Block result;
    private final int elementAmount;

    protected AbstractCrackingRecipe(HolderSet<Block> input, Block result, int elementAmount) {
        this.input = input;
        this.result = result;
        this.elementAmount = elementAmount;
    }

    public static <T extends AbstractCrackingRecipe> MapCodec<T> codec(AbstractCrackingRecipe.Factory<T> factory) {
        return RecordCodecBuilder.mapCodec(builder -> builder.group(
                RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf(ECNames.INPUT).forGetter(AbstractCrackingRecipe::input),
                BuiltInRegistries.BLOCK.byNameCodec().optionalFieldOf(ECNames.RESULT, Blocks.AIR).forGetter(AbstractCrackingRecipe::result),
                Codec.INT.optionalFieldOf(ECNames.ELEMENT_AMOUNT, 0).forGetter(AbstractCrackingRecipe::elementAmount)
        ).apply(builder, factory::create));
    }

    public static <T extends AbstractCrackingRecipe> StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull T> streamCodec(AbstractCrackingRecipe.Factory<T> factory) {
        return StreamCodec.composite(
                ByteBufCodecs.holderSet(Registries.BLOCK), AbstractCrackingRecipe::input,
                ByteBufCodecs.registry(Registries.BLOCK), AbstractCrackingRecipe::result,
                ByteBufCodecs.INT.apply(i -> i), AbstractCrackingRecipe::elementAmount,
                factory::create);
    }

    @Override
    public boolean matches(@NotNull CrackingRecipeInput recipeInput, @NotNull Level level) {
        return recipeInput.state().is(input);
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    public HolderSet<Block> input() {
        return input;
    }

    public Block result() {
        return result;
    }

    public int elementAmount() {
        return elementAmount;
    }

    public boolean hasResult() {
        return !result.defaultBlockState().isAir();
    }

    public interface Factory<T extends AbstractCrackingRecipe> {
        T create(HolderSet<Block> input, Block result, int elementAmount);
    }

}
