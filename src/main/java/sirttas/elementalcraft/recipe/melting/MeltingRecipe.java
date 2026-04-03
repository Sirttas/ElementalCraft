package sirttas.elementalcraft.recipe.melting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.recipe.ECRecipeBookCategories;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.ingredient.BlockHolderSetIngredient;

public record MeltingRecipe(
        CommonInfo commonInfo,
        BlockHolderSetIngredient input,
        FluidStackTemplate result,
        int cooldown,
        int elementAmount
) implements Recipe<@NotNull MeltingRecipeInput> {

    public static final String NAME = "melting";
    public static final MapCodec<MeltingRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            CommonInfo.MAP_CODEC.forGetter(r -> r.commonInfo),
            BlockHolderSetIngredient.CODEC.fieldOf(ECNames.INPUT).forGetter(MeltingRecipe::input),
            FluidStackTemplate.CODEC.fieldOf(ECNames.RESULT).forGetter(MeltingRecipe::result),
            Codec.INT.fieldOf("cooldown").forGetter(MeltingRecipe::cooldown),
            Codec.INT.fieldOf("element_amount").forGetter(MeltingRecipe::elementAmount)
    ).apply(builder, MeltingRecipe::new));
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull MeltingRecipe> STREAM_CODEC = StreamCodec.composite(
            CommonInfo.STREAM_CODEC, r -> r.commonInfo,
            BlockHolderSetIngredient.STREAM_CODEC, MeltingRecipe::input,
            FluidStackTemplate.STREAM_CODEC, MeltingRecipe::result,
            ByteBufCodecs.INT, MeltingRecipe::cooldown,
            ByteBufCodecs.INT, MeltingRecipe::elementAmount,
            MeltingRecipe::new);

    @Override
    public boolean matches(@NotNull MeltingRecipeInput recipeInput, @NotNull Level level) {
        return recipeInput.elementAmount() >= elementAmount * recipeInput.elementConsumption() && recipeInput.state().is(input.blocks());
    }

    @Override
    public @NotNull ItemStack assemble(MeltingRecipeInput input) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean showNotification() {
        return commonInfo.showNotification();
    }

    @Override
    public @NotNull String group() {
        return NAME;
    }

    @Override
    public @NotNull RecipeSerializer<@NotNull MeltingRecipe> getSerializer() {
        return ECRecipeSerializers.MELTING.get();
    }

    @Override
    public @NotNull RecipeType<@NotNull MeltingRecipe> getType() {
        return ECRecipeTypes.MELTING.get();
    }

    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.create(new Ingredient(input));
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return ECRecipeBookCategories.MELTING.get();
    }

    public FluidState fluidState() {
        return result.fluid().value().defaultFluidState();
    }
}
