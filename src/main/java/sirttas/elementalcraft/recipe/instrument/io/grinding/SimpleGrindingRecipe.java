package sirttas.elementalcraft.recipe.instrument.io.grinding;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.recipe.ECRecipeBookCategories;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.instrument.io.AbstractIOInstrumentRecipe;
import sirttas.elementalcraft.recipe.instrument.io.IOInstrumentRecipeDisplay;

import javax.annotation.Nonnull;
import java.util.List;

public class SimpleGrindingRecipe extends AbstractIOInstrumentRecipe implements GrindingRecipe {

    public static final MapCodec<SimpleGrindingRecipe> CODEC = codec(SimpleGrindingRecipe::new);
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull SimpleGrindingRecipe> STREAM_CODEC = streamCodec(SimpleGrindingRecipe::new);

    public SimpleGrindingRecipe(CommonInfo commonInfo, int elementAmount, double luckRatio, Ingredient ingredient, int inputSize, ItemStackTemplate output) {
        super(commonInfo, elementAmount, luckRatio, ingredient, inputSize, output);
    }

    @Override
    public @NotNull String group() {
        return GrindingRecipe.NAME;
    }

    @Nonnull
    @Override
    public RecipeSerializer<@NotNull SimpleGrindingRecipe> getSerializer() {
        return ECRecipeSerializers.GRINDING.get();
    }


    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return ECRecipeBookCategories.GRINDING.get();
    }

    @Override
    public @NotNull List<RecipeDisplay> display() {
        return List.of(new IOInstrumentRecipeDisplay(
                ElementType.WATER,
                getElementAmount(),
                input.display(),
                new SlotDisplay.ItemStackSlotDisplay(result),
                new SlotDisplay.ItemSlotDisplay(ECBlocks.WATER_MILL_GRINDSTONE.get().asItem())));
    }
}
