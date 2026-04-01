package sirttas.elementalcraft.recipe.cracking;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderSet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.ECRecipeTypes;

public class SculkCrackingRecipe extends AbstractCrackingRecipe {

    public static final String NAME = "sculk_cracking";
    public static final MapCodec<SculkCrackingRecipe> CODEC = codec(SculkCrackingRecipe::new);
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull SculkCrackingRecipe> STREAM_CODEC = streamCodec(SculkCrackingRecipe::new);

    public SculkCrackingRecipe(HolderSet<Block> input, Block result, int elementAmount) {
        super(input, result, elementAmount);
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ECRecipeSerializers.SCULK_CRACKING.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ECRecipeTypes.SCULK_CRACKING.get();
    }
}
