package sirttas.elementalcraft.recipe.instrument.io.sawing;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.recipe.ECRecipeBookCategories;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.io.AbstractIOInstrumentRecipe;
import sirttas.elementalcraft.recipe.instrument.io.IOInstrumentRecipeDisplay;

import javax.annotation.Nonnull;
import java.util.List;

public class SawingRecipe extends AbstractIOInstrumentRecipe {

    public static final String NAME = "sawing";
    public static final MapCodec<SawingRecipe> CODEC = codec(SawingRecipe::new);
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull SawingRecipe> STREAM_CODEC = streamCodec(SawingRecipe::new);

    public SawingRecipe(CommonInfo commonInfo, int elementAmount, double luckRatio, Ingredient ingredient, int inputSize, ItemStackTemplate output) {
        super(commonInfo, elementAmount, luckRatio, ingredient, inputSize, output);
    }

    @Override
    public @NotNull String group() {
        return NAME;
    }

    @Nonnull
    @Override
    public RecipeType<@NotNull SawingRecipe> getType() {
        return ECRecipeTypes.SAWING.get();
    }

    @Nonnull
    @Override
    public RecipeSerializer<@NotNull SawingRecipe> getSerializer() {
        return ECRecipeSerializers.SAWING.get();
    }


    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return ECRecipeBookCategories.SAWING.get();
    }

    @Override
    public @NotNull List<RecipeDisplay> display() {
        return List.of(new IOInstrumentRecipeDisplay(
                ElementType.WATER,
                getElementAmount(),
                input.display(),
                new SlotDisplay.ItemStackSlotDisplay(result),
                new SlotDisplay.ItemSlotDisplay(ECBlocks.WATER_MILL_WOOD_SAW.get().asItem())));
    }

    @Override
    public List<ElementType> getValidElementTypes() {
        return List.of(ElementType.WATER, ElementType.AIR);
    }
}
