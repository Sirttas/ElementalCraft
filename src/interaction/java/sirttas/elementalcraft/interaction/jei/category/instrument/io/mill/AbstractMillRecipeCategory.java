package sirttas.elementalcraft.interaction.jei.category.instrument.io.mill;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.ItemLike;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.category.instrument.io.AbstractIOInstrumentRecipeCategory;
import sirttas.elementalcraft.recipe.instrument.InstrumentRecipe;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractMillRecipeCategory<I extends RecipeInput, T extends InstrumentRecipe<I>> extends AbstractIOInstrumentRecipeCategory<I, T> {

    private final List<ItemStack> instruments;

    protected AbstractMillRecipeCategory(IGuiHelper guiHelper, String translationKey, ItemLike... instruments) {
        super(guiHelper, translationKey, instruments[0]);
        this.instruments = Arrays.stream(instruments)
                .map(ItemStack::new)
                .toList();
    }

    @Override
    protected List<ItemStack> getContainers() {
        return List.of(container, new ItemStack(ECBlocks.SMALL_CONTAINER.get()));
    }

    protected void createFocusLinks(@Nonnull IRecipeLayoutBuilder builder, @Nonnull T recipe, Slots slots) {
        var containers = getContainers();
        var types = getElementTypeIngredients(recipe);


        if (instruments.size() == containers.size() && instruments.size() == types.size()) {
            builder.createFocusLink(slots.instruments(), slots.containers(), slots.elements());
        }
    }
}
