package sirttas.elementalcraft.interaction.jei.category.instrument.io.mill;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.ItemLike;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.category.instrument.io.AbstractIOInstrumentRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
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

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull T recipe, @Nonnull IFocusGroup focuses) {
        var ingredients = recipe.getIngredients();

        builder.addSlot(RecipeIngredientRole.INPUT, 0, 0)
                .addIngredients(ingredients.getFirst());

        var containers = getContainers();
        var types = getElementTypeIngredients(recipe);

        var instrumentsSlot = builder.addSlot(RecipeIngredientRole.CATALYST, 30, 24)
                .addItemStacks(instruments);
        var containersSlot = builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 30, 40)
                .addItemStacks(containers);
        var typesSlot = builder.addSlot(RecipeIngredientRole.INPUT, 30, 58)
                .addIngredients(ECIngredientTypes.ELEMENT, types);

        if (instruments.size() == containers.size() && instruments.size() == types.size()) {
            builder.createFocusLink(instrumentsSlot, containersSlot, typesSlot);
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 59, 0)
                .addItemStacks(getOutputs(recipe));
    }
}
