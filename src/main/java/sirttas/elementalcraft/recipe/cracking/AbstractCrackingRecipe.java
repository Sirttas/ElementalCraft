package sirttas.elementalcraft.recipe.cracking;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.recipe.ingredient.BlockHolderSetIngredient;

public abstract class AbstractCrackingRecipe implements Recipe<@NotNull CrackingRecipeInput> {

    protected final Recipe.CommonInfo commonInfo;
    private final BlockHolderSetIngredient input;
    private final Block result;
    private final int elementAmount;

    protected AbstractCrackingRecipe(Recipe.CommonInfo commonInfo, BlockHolderSetIngredient input, Block result, int elementAmount) {
        this.commonInfo = commonInfo;
        this.input = input;
        this.result = result;
        this.elementAmount = elementAmount;
    }

    public static <T extends AbstractCrackingRecipe> MapCodec<T> codec(AbstractCrackingRecipe.Factory<T> factory) {
        return RecordCodecBuilder.mapCodec(builder -> builder.group(
                CommonInfo.MAP_CODEC.forGetter(r -> r.commonInfo),
                BlockHolderSetIngredient.CODEC.fieldOf(ECNames.INPUT).forGetter(AbstractCrackingRecipe::input),
                BuiltInRegistries.BLOCK.byNameCodec().optionalFieldOf(ECNames.RESULT, Blocks.AIR).forGetter(AbstractCrackingRecipe::result),
                Codec.INT.optionalFieldOf(ECNames.ELEMENT_AMOUNT, 0).forGetter(AbstractCrackingRecipe::elementAmount)
        ).apply(builder, factory::create));
    }

    public static <T extends AbstractCrackingRecipe> StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull T> streamCodec(AbstractCrackingRecipe.Factory<T> factory) {
        return StreamCodec.composite(
                CommonInfo.STREAM_CODEC, r -> r.commonInfo,
                BlockHolderSetIngredient.STREAM_CODEC, AbstractCrackingRecipe::input,
                ByteBufCodecs.registry(Registries.BLOCK), AbstractCrackingRecipe::result,
                ByteBufCodecs.INT.apply(i -> i), AbstractCrackingRecipe::elementAmount,
                factory::create);
    }

    @Override
    public boolean matches(@NotNull CrackingRecipeInput recipeInput, @NotNull Level level) {
        return recipeInput.state().is(input.blocks());
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CrackingRecipeInput input) {
        return new ItemStack(result);
    }

    @Override
    public boolean showNotification() {
        return this.commonInfo.showNotification();
    }

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.create(new Ingredient(input));
    }

    private @NotNull BlockHolderSetIngredient input() {
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
        T create(Recipe.CommonInfo commonInfo, BlockHolderSetIngredient input, Block result, int elementAmount);
    }

}
