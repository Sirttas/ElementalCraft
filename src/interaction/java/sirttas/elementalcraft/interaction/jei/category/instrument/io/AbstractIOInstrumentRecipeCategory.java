package sirttas.elementalcraft.interaction.jei.category.instrument.io;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.ItemLike;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.category.instrument.AbstractInstrumentRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.recipe.instrument.InstrumentRecipe;
import sirttas.elementalcraft.recipe.instrument.io.IOInstrumentRecipeDisplay;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class AbstractIOInstrumentRecipeCategory<I extends RecipeInput, T extends InstrumentRecipe<I>> extends AbstractInstrumentRecipeCategory<I, T> {

	protected final ItemStack container = new ItemStack(ECBlocks.CONTAINER.get());

	protected AbstractIOInstrumentRecipeCategory(IGuiHelper guiHelper, String translationKey, ItemLike item) {
		this(guiHelper, translationKey, new ItemStack(item));
	}
	
	protected AbstractIOInstrumentRecipeCategory(IGuiHelper guiHelper, String translationKey, ItemStack instrument) {
		super(translationKey, createDrawableStack(guiHelper, instrument), 75, 75);
		addOverlay(guiHelper.createDrawable(ElementalCraftApi.createRL("textures/gui/overlay/io.png"), 0, 0, 65, 16), 8, 20);
	}

	protected List<ItemStack> getContainers() {
		return List.of(container);
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull T recipe, @Nonnull IFocusGroup focuses) {
        if (!(recipe.display().getFirst() instanceof IOInstrumentRecipeDisplay display)) {
            return;
        }

        var inputSlot = builder.addSlot(RecipeIngredientRole.INPUT, 0, 0)
				.add(display.input());

        var instrumentsSlot = builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, 30, 24)
				.add(display.craftingStation());
        var containersSlot = builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, 30, 40)
                .addItemStacks(getContainers());

        var elementsSlot = builder.addSlot(RecipeIngredientRole.INPUT, 30, 58)
                .addIngredients(ECIngredientTypes.ELEMENT, getElementTypeIngredients(recipe));

        var outputSlot = builder.addSlot(RecipeIngredientRole.OUTPUT, 59, 0)
				.add(display.result());

        try {
            createFocusLinks(builder, recipe, new Slots(inputSlot, instrumentsSlot, containersSlot, elementsSlot, outputSlot));
        } catch (IllegalArgumentException e) {
            ElementalCraftApi.LOGGER.trace("Failed to create focus link for recipe: {}", e.getMessage());
        }
	}

    protected void createFocusLinks(@Nonnull IRecipeLayoutBuilder builder, @Nonnull T recipe, Slots slots) {
    }

    protected record Slots(
            IRecipeSlotBuilder input,
            IRecipeSlotBuilder instruments,
            IRecipeSlotBuilder containers,
            IRecipeSlotBuilder elements,
            IRecipeSlotBuilder output
    ) {}
}
